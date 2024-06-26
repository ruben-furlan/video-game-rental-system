package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.domain.port.in.inventory.GameInventoryUserCase;
import com.videogamerentalsystem.domain.port.in.rental.RentalLoyaltyUserCase;
import com.videogamerentalsystem.domain.port.in.rental.RentalPaymentCalculationUserCase;
import com.videogamerentalsystem.domain.port.in.rental.RentalUserCase;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCustomerCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalProductCommand;
import com.videogamerentalsystem.domain.port.out.rental.RentalRepositoryPort;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiExceptionConstantsMessagesError;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class RentalService implements RentalUserCase {

    private final RentalRepositoryPort rentalRepositoryPort;

    private final GameInventoryUserCase gameInventoryUserCase;

    private final RentalPaymentCalculationUserCase rentalPaymentCalculationUserCase;

    private final RentalLoyaltyUserCase rentalLoyaltyUserCase;

    @Override
    public RentalModel create(RentalCommand rentalCommand) {

        RentalModel buildToModel = this.buildToModelAndValidate(rentalCommand);

        List<RentalProductModel> productModels = buildToModel.getProductModels();

        List<GameInventoryModel> gameInventoryModels = this.gameInventoryUserCase.stockExists(this.getTitles(productModels));

        this.rentalPaymentCalculationUserCase.applyAndCalculateRentalCost(buildToModel, gameInventoryModels);

        buildToModel.getCustomerModel().addLoyaltyPoints(this.rentalLoyaltyUserCase.calculateTotalPoints(productModels));

        this.gameInventoryUserCase.stockRemove(gameInventoryModels);

        return this.rentalRepositoryPort.create(buildToModel);
    }


    @Override
    public RentalModel handBackGame(Long rentalId, Long rentalProductId) {
        RentalModel rentalModel = this.rentalRepositoryPort.findRentalById(rentalId)
                .orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.Rental.RENTAl_NOT_FOUND.formatted(rentalId), HttpStatus.NOT_FOUND));

        RentalProductModel productModel = this.fidRentalProductModel(rentalProductId, rentalModel);

        if (productModel.getStatus().isFinish()) {
            throw new ApiException(ApiExceptionConstantsMessagesError.Rental.PRODUCT_FINISH.formatted(rentalProductId, rentalId), HttpStatus.BAD_REQUEST);
        }

        GameInventoryModel gameInventoryModel = this.gameInventoryUserCase.findInventoryByTitle(productModel.getTitle())
                .orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.Rental.PRODUCT_TITLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        this.rentalPaymentCalculationUserCase.applySurchargeForProduct(productModel);

        RentalProductChargeModel rentalProductChargeModel = productModel.getCharges();

        this.rentalRepositoryPort.updateStatusProductAndPrice(rentalId, productModel.getId(), this.evaluateAndApplyIfPriceChangeOrKeepCurrent(rentalProductChargeModel.getTotal(), rentalProductChargeModel.getPrice()), RentalProductStatus.FINISH);

        productModel.updateStatus(RentalProductStatus.FINISH);

        this.gameInventoryUserCase.stockAdd(gameInventoryModel);

        return rentalModel;
    }

    @Override
    public RentalModel get(Long rentalId) {
        return this.rentalRepositoryPort.findRentalById(rentalId)
                .orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.Rental.RENTAl_NOT_FOUND.formatted(rentalId), HttpStatus.NOT_FOUND));
    }


    private RentalModel buildToModelAndValidate(RentalCommand rentalCommand) {

        this.validateCommand(rentalCommand);

        RentalCustomerCommand rentalCustomerCommand = rentalCommand.customer();

        List<RentalProductCommand> rentalProductCommands = rentalCommand.products();

        List<RentalProductModel> productModels = rentalProductCommands
                .stream().map(rentalProductCommand -> RentalProductModel.builder()
                        .title(rentalProductCommand.title())
                        .endDate(rentalProductCommand.endDate())
                        .build()).collect(Collectors.toList());


        RentalCustomerModel customerModel = RentalCustomerModel.builder()
                .firstName(rentalCustomerCommand.firstName())
                .latName(rentalCustomerCommand.lastName())
                .documentNumber(rentalCustomerCommand.DocumentNumber())
                .build();

        return RentalModel.builder().date(LocalDateTime.now())
                .currency(rentalCommand.currency())
                .paymentType(rentalCommand.paymentType())
                .customerModel(customerModel)
                .productModels(productModels)
                .build();
    }

    private void validateCommand(RentalCommand rentalCommand) {
        Map<String, Long> titleCounts = rentalCommand.products().stream()
                .collect(Collectors.groupingBy(RentalProductCommand::title, Collectors.counting()));
        List<String> duplicateTitles = titleCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!duplicateTitles.isEmpty()) {
            throw new ApiException( "Duplicated titles found: %s".formatted(duplicateTitles), HttpStatus.BAD_REQUEST);
        }
    }


    private BigDecimal evaluateAndApplyIfPriceChangeOrKeepCurrent(BigDecimal total, BigDecimal priceToUpate) {
        if (Objects.nonNull(total) && total.compareTo(BigDecimal.ZERO) > 0) {
            priceToUpate = total;
        }
        return priceToUpate;
    }

    private List<String> getTitles(List<RentalProductModel> productModels) {
        return productModels.stream()
                .map(RentalProductModel::getTitle)
                .collect(Collectors.toList());
    }

    private RentalProductModel fidRentalProductModel(Long rentalId, RentalModel rentalModel) {
        return rentalModel.getProductModels().stream()
                .filter(productModel -> productModel.getId().equals(rentalId))
                .findFirst().orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.Generic.MESSAGE_GENERIC, HttpStatus.INTERNAL_SERVER_ERROR));
    }


}

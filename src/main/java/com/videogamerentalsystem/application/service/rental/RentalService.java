package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.application.service.inventory.GameInventoryService;
import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
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

    private final GameInventoryService gameInventoryService;

    private final RentalPaymentCalculationService rentalPaymentCalculationService;

    private final RentalLoyaltyService rentalLoyaltyService;

    @Override
    public RentalModel create(RentalCommand rentalCommand) {

        RentalModel buildToModel = this.buildToModelAndValidate(rentalCommand);

        List<RentalProductModel> productModels = buildToModel.getProductModels();

        List<GameInventoryModel> gameInventoryModels = this.gameInventoryService.stockExists(this.getTitles(productModels));

        this.rentalPaymentCalculationService.applyAndCalculateRentalCost(buildToModel, gameInventoryModels);

        Integer loyaltyPoints = this.rentalLoyaltyService.calculateTotalPoints(productModels);

        buildToModel.getCustomerModel().addLoyaltyPoints(loyaltyPoints);

        RentalModel rentalModel = this.rentalRepositoryPort.create(buildToModel);

        this.gameInventoryService.stockRemove(gameInventoryModels);

        return rentalModel;
    }

    private  List<String> getTitles(List<RentalProductModel> productModels) {
        return productModels.stream().map(RentalProductModel::getTitle).collect(Collectors.toList());
    }


    @Override
    public RentalModel  handBackGame(Long rentalId,  Long rentalProductId) {
        RentalModel rentalModel = this.rentalRepositoryPort.findRentalById(rentalId).orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.RENTAL_ID_NOT_FOUND.formatted(rentalId), HttpStatus.NOT_FOUND));

        RentalProductModel productModel = this.fidRentalProductModel(rentalProductId, rentalModel);

        if(productModel.getStatus().isFinish()){
            throw new ApiException("The product  %d finished, please check the rent: %d".formatted(rentalProductId, rentalId), HttpStatus.BAD_REQUEST);
        }

        GameInventoryModel gameInventoryModel = this.gameInventoryService.findInventoryByTitle(productModel.getTitle()).orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.PRODUCT_TITLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        this.rentalPaymentCalculationService.applySurchargeForProduct(productModel);

        RentalProductChargeModel rentalProductChargeModel = productModel.getCharges();

        this.rentalRepositoryPort.updateStatusProductAndPrice(rentalId, productModel.getId(), this.evaluateAndApplyIfPriceChangeOrKeepCurrent(rentalProductChargeModel.getTotal(), rentalProductChargeModel.getPrice()), RentalProductStatus.FINISH);

        productModel.updateStatus(RentalProductStatus.FINISH);

        this.gameInventoryService.stockAdd(gameInventoryModel);

        return rentalModel;
    }

    private  RentalProductModel fidRentalProductModel(Long rentalId, RentalModel rentalModel) {
        return rentalModel.getProductModels().stream().filter(productModel -> productModel.getId().equals(rentalId)).findFirst().orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.MESSAGE_GENERIC, HttpStatus.INTERNAL_SERVER_ERROR));
    }


    @Override
    public RentalModel get(Long rentalId) {
       return  this.rentalRepositoryPort.findRentalById(rentalId).orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.RENTAL_ID_NOT_FOUND.formatted(rentalId), HttpStatus.NOT_FOUND));
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
            String errorMessage = "Duplicated titles found: %s".formatted(duplicateTitles);
            throw new ApiException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }


    private  BigDecimal evaluateAndApplyIfPriceChangeOrKeepCurrent(BigDecimal total, BigDecimal priceToUpate) {
        if(Objects.nonNull(total) && total.compareTo(BigDecimal.ZERO)>0) {
            priceToUpate = total;
        }
        return priceToUpate;
    }


}

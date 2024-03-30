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
import java.util.Set;
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

        Set<RentalProductModel> productModels = buildToModel.getProductModels();

        Set<GameInventoryModel> gameInventoryModels = this.gameInventoryService.stockExists(productModels);

        this.rentalPaymentCalculationService.applyAndCalculateRentalCost(buildToModel, gameInventoryModels);

        Integer loyaltyPoints = this.rentalLoyaltyService.calculateTotalPoints(productModels);

        buildToModel.getCustomerModel().addLoyaltyPoints(loyaltyPoints);

        RentalModel rentalModel = this.rentalRepositoryPort.create(buildToModel);

        this.gameInventoryService.stockRemove(gameInventoryModels);

        return rentalModel;
    }


    @Override
    public RentalModel  handBackGame(Long rentalId,  Long productId) {
        RentalModel rentalModel = this.rentalRepositoryPort.findRentalById(rentalId).orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.RENTAL_NOT_FOUND, HttpStatus.NOT_FOUND));

        GameInventoryModel inventoryModel = this.gameInventoryService.findInventoryByProductId(productId).orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.PRODUCT_TITLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        RentalProductModel productModel = this.fidRentalProductModelByTitle(rentalModel, inventoryModel.getId());

        this.rentalPaymentCalculationService.applySurchargeForProduct(productModel);

        RentalProductChargeModel charges = productModel.getCharges();

        this.rentalRepositoryPort.updateStatusProductAndPrice(rentalId, productModel.getId(), this.evaluateAndApplyIfPriceChangeOrKeepCurrent(charges.getTotal(), charges.getPrice()), RentalProductStatus.FINISH);

        productModel.updateStatus(RentalProductStatus.FINISH);

        return rentalModel;
    }


    @Override
    public RentalModel get(Long rentalId) {
       return  this.rentalRepositoryPort.findRentalById(rentalId).orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.RENTAL_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private  RentalProductModel fidRentalProductModelByTitle(RentalModel rentalModel, Long productId) {
        return rentalModel.getProductModels()
                .stream().filter(productModel -> Objects.equals(productModel.getId(), productId)).findFirst()
                .orElseThrow(() -> new ApiException(ApiExceptionConstantsMessagesError.MESSAGE_GENERIC, HttpStatus.INTERNAL_SERVER_ERROR));
    }


    private RentalModel buildToModelAndValidate(RentalCommand rentalCommand) {
        this.validateCommand(rentalCommand);
        RentalCustomerCommand rentalCustomerCommand = rentalCommand.customer();

        Set<RentalProductCommand> rentalProductCommands = rentalCommand.products();

        Set<RentalProductModel> productModels = rentalProductCommands
                .stream().map(rentalProductCommand -> RentalProductModel.builder()
                        .title(rentalProductCommand.title())
                        .endDate(rentalProductCommand.endDate())
                        .build()).collect(Collectors.toSet());


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

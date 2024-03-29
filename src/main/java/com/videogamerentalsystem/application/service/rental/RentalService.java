package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.application.service.inventory.GameInventoryService;
import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.rental.RentalUserCase;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCustomerCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalProductCommand;
import com.videogamerentalsystem.domain.port.out.rental.RentalRepositoryPort;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


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

        this.rentalPaymentCalculationService.calculateAndSetRentalCost(buildToModel, gameInventoryModels);

        Integer loyaltyPoints = this.rentalLoyaltyService.calculateTotalPoints(productModels);

        buildToModel.getCustomerModel().addLoyaltyPoints(loyaltyPoints);

        RentalModel rentalModel = this.rentalRepositoryPort.create(buildToModel);


        this.gameInventoryService.stockRemove(gameInventoryModels);

        return rentalModel;
    }


    @Override
    public RentalProductModel findGameByTitleAndRentalId(String title, Long rentalId) {
        return null;
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

    private  void validateCommand(RentalCommand rentalCommand) {
        Map<String, Long> titleCounts = rentalCommand.products().stream()
                .collect(Collectors.groupingBy(RentalProductCommand::title, Collectors.counting()));
        List<String> duplicateTitles = titleCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!duplicateTitles.isEmpty()) {
            String errorMessage = "Títulos duplicados encontrados: %s".formatted(duplicateTitles);
            throw new ApiException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }


}

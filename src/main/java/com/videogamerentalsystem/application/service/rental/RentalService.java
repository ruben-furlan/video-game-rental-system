package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.rental.RentalUserCase;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCustomerCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalProductCommand;
import com.videogamerentalsystem.domain.port.out.inventory.GameInventoryRepositoryPort;
import com.videogamerentalsystem.domain.port.out.rental.RentalRepositoryPort;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class RentalService implements RentalUserCase {

    private final RentalRepositoryPort rentalRepositoryPort;
    private final GameInventoryRepositoryPort gameInventoryRepositoryPort;
    private final RentalPaymentCalculationService rentalPaymentCalculationService;

    @Override
    public RentalModel create(RentalCommand rentalCommand) {
        RentalModel buildToModel = this.buildToModel(rentalCommand);

        RentalModel rentalModel = this.rentalRepositoryPort.create(buildToModel);

        this.rentalPaymentCalculationService.calculateAndSetRentalCost(rentalModel);


        return  rentalModel;
    }

    @Override
    public RentalProductModel findGameByTitleAndRentalId(String title, Long rentalId) {
        return null;
    }


    private RentalModel buildToModel(RentalCommand rentalCommand){
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
                .build();

      return RentalModel.builder().date(LocalDateTime.now())
                .currency(rentalCommand.currency())
                .paymentType(rentalCommand.paymentType())
                .customerModel(customerModel)
                .productModels(productModels)
                .build();
    }



}

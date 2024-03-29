package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.rental.RentalUserCase;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCustomerCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalProductCommand;
import com.videogamerentalsystem.domain.port.out.inventory.GameInventoryRepositoryPort;
import com.videogamerentalsystem.domain.port.out.rental.RentalRepositoryPort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
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

    @Override
    public RentalModel create(RentalCommand rentalCommand) {
        Optional<GameInventoryModel> byID = this.gameInventoryRepositoryPort.findByID(1L);
        BigDecimal amount = byID.get().getInventoryPriceModel().getAmount();


        RentalCustomerCommand rentalCustomerCommand = rentalCommand.customer();

        Set<RentalProductCommand> rentalProductCommands = rentalCommand.products();

        Set<RentalProductModel> productModels = rentalProductCommands.stream()
                .map(rentalProductCommand -> RentalProductModel.builder().title(rentalProductCommand.title())
                .endDate(rentalProductCommand.endDate())
                .price(amount).build()).collect(Collectors.toSet());


        RentalCustomerModel customerModel = RentalCustomerModel.builder()
                .firstName(rentalCustomerCommand.firstName())
                .latName(rentalCustomerCommand.lastName())
                .build();

        RentalModel rentalModel = RentalModel.builder().date(LocalDateTime.now())
                .currency(rentalCommand.currency())
                .paymentType(rentalCommand.paymentType())
                .customerModel(customerModel)
                .productModels(productModels)
                .build();


        return  this.rentalRepositoryPort.create(rentalModel);
    }

    @Override
    public RentalProductModel findGameByTitleAndRentalId(String title, Long rentalId) {
        return null;
    }
}

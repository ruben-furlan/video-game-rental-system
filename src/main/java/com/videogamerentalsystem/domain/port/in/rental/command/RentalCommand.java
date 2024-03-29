package com.videogamerentalsystem.domain.port.in.rental.command;

import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record RentalCommand(
                           @NotBlank String currency,
                           @NotNull RentalPaymentType paymentType,
                           @Valid  @NotNull RentalCustomerCommand customer,
                           @Valid @NotNull Set<RentalProductCommand> products) {}
package com.videogamerentalsystem.domain.port.in.rental.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record RentalCommand(
                           @NotBlank String currency,
                           @NotBlank String paymentType,
                           @NotNull RentalCustomerCommand customer,
                           @NotNull Set<RentalProductCommand> products) {}
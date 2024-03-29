package com.videogamerentalsystem.domain.port.in.rental.command;

import jakarta.validation.constraints.NotNull;

public record RentalCustomerCommand(@NotNull String firstName,
                                    @NotNull String lastName) {
}
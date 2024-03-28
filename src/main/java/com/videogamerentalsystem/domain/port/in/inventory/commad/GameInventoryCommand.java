package com.videogamerentalsystem.domain.port.in.inventory.commad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GameInventoryCommand(
        @NotBlank String title,
        @NotBlank String type,
        @NotNull @Positive Integer stock,
        @NotNull GameInventoryPriceCommand price
) {}
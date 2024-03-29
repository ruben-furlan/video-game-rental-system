package com.videogamerentalsystem.domain.port.in.inventory.commad;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GameInventoryCommand(
        @NotBlank String title,
        GameType type,
        @NotNull @Positive Integer stock,
        @NotNull GameInventoryPriceCommand price
) {}
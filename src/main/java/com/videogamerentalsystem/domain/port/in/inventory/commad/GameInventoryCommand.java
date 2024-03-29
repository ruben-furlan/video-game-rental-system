package com.videogamerentalsystem.domain.port.in.inventory.commad;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GameInventoryCommand(
        @NotBlank String title,
        @NotNull  GameType type,
        @NotNull @Positive Integer stock,
        @Valid @NotNull GameInventoryPriceCommand price
) {}
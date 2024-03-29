package com.videogamerentalsystem.domain.port.in.inventory.commad;

import com.videogamerentalsystem.domain.model.inventory.constant.GamePriceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record GameInventoryPriceCommand(
        GamePriceType type,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount,
        @NotBlank String currency
) {}
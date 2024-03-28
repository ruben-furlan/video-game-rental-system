package com.videogamerentalsystem.domain.port.in.inventory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record GameInventoryPriceCommand(
        @NotBlank String type,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal cost,
        @NotBlank String currency
) {}
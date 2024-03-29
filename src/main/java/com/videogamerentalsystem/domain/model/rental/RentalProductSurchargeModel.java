package com.videogamerentalsystem.domain.model.rental;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalProductSurchargeModel {
    private String reason;
    private BigDecimal amount;
}

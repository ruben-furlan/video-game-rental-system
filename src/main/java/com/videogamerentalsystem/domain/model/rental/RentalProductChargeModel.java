package com.videogamerentalsystem.domain.model.rental;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder
@Accessors(chain = true)
public class RentalProductChargeModel {
    private BigDecimal price;
    private RentalProductSurchargeModel surcharges;
    private BigDecimal total;
}

package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseProductChargeDTO {
    private BigDecimal price;
    private ResponseProductSurchargesDTO surcharges;
    /**
     * The total is only charged when there are surcharges applied.
     * This is because the price is itemized for the customer.
     */
    private BigDecimal total;
}

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

    /**
     * The total is only charged when there are surcharges applied.
     * This is because the price is itemized for the customer.
     */
    private BigDecimal total;

    public void updateTotalAndPrice(BigDecimal total, BigDecimal price) {
        this.total = total;
        this.price = price;
    }

    public void addSurchargeModel(RentalProductSurchargeModel surcharges){
        this.surcharges = surcharges;
    }

}

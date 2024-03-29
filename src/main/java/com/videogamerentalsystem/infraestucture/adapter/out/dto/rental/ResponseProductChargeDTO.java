package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseProductChargeDTO {
    private BigDecimal price;
    private ResponseProductSurchargesDTO surcharges;
    private BigDecimal total;
}

package com.videogamerentalsystem.infraestucture.adapter.out.dto.inventory;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseInventoryPriceDTO {
    private String type;
    private BigDecimal amount;
    private String currency;
}

package com.videogamerentalsystem.infraestucture.adapter.out.dto.inventory;

import com.videogamerentalsystem.domain.model.inventory.constant.GamePriceType;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseInventoryPriceDTO {
    private GamePriceType type;
    private BigDecimal amount;
    private String currency;
}

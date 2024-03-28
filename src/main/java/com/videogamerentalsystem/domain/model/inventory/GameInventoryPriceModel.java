package com.videogamerentalsystem.domain.model.inventory;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameInventoryPriceModel {
    private String type;
    private BigDecimal amount;
    private String currency;
}

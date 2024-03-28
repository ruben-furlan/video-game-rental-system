package com.videogamerentalsystem.domain.model.inventory;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameInventoryCostConfigurationModel {
    private String type;
    private BigDecimal cost;
    private String currency;
}

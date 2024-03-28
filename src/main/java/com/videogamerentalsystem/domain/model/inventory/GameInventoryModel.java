package com.videogamerentalsystem.domain.model.inventory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameInventoryModel {
    private Long id;
    private String title;
    private String type;
    private Integer stock;
    private GameInventoryPriceModel inventoryPriceModel;
}


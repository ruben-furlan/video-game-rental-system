package com.videogamerentalsystem.domain.model.inventory;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameInventoryModel {
    private Long id;
    private String title;
    private GameType type;
    private Integer stock;
    private GameInventoryPriceModel inventoryPriceModel;
}


package com.videogamerentalsystem.infraestucture.adapter.out.mapper.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.inventory.ResponseInventoryDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.inventory.ResponseInventoryPriceDTO;
import java.util.Objects;
import org.springframework.stereotype.Component;


@Component
public class GameInventoryResponseApiMapper {
    public ResponseInventoryDTO toResponseApi(GameInventoryModel gameInventoryModel) {
        return Objects.isNull(gameInventoryModel) ? null : ResponseInventoryDTO.builder()
                .id(gameInventoryModel.getId())
                .title(gameInventoryModel.getTitle())
                .stock(gameInventoryModel.getStock())
                .price(this.toResponsePriceModelApi(gameInventoryModel.getInventoryPriceModel()))
                .build();
    }

    private ResponseInventoryPriceDTO toResponsePriceModelApi(GameInventoryPriceModel inventoryPriceModel) {
        return ResponseInventoryPriceDTO.builder()
                .type(inventoryPriceModel.getType())
                .amount(inventoryPriceModel.getAmount())
                .currency(inventoryPriceModel.getCurrency())
                .build();
    }

}

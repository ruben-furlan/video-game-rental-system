package com.videogamerentalsystem.infraestucture.adapter.out.mapper.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryPriceEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


@Component
public class GameInventoryMapper {

    public GameInventoryEntity toEntityGameInventory(GameInventoryModel gameInventoryModel) {
        GameInventoryPriceEntity priceEntity = this.toEntityGameInventoryPrice(gameInventoryModel.getInventoryPriceModel());
        GameInventoryEntity inventoryEntity = GameInventoryEntity.builder()
                .title(gameInventoryModel.getTitle())
                .type(gameInventoryModel.getType())
                .stock(gameInventoryModel.getStock())
                .inventoryPriceEntity(priceEntity)
                .build();

        priceEntity.addJoin(inventoryEntity);

        return inventoryEntity;
    }

    private GameInventoryPriceEntity toEntityGameInventoryPrice(GameInventoryPriceModel gameInventoryPriceModel) {
        return GameInventoryPriceEntity.builder()
                .type(gameInventoryPriceModel.getType())
                .amount(gameInventoryPriceModel.getAmount())
                .currency(gameInventoryPriceModel.getCurrency())
                .build();
    }

    public List<GameInventoryModel> toModelGameInventories(List<GameInventoryEntity> gameInventoryEntity) {
        return gameInventoryEntity.stream().map(this::toModelGameInventory).collect(Collectors.toList());
    }

    public GameInventoryModel toModelGameInventory(GameInventoryEntity gameInventoryEntity) {
        return GameInventoryModel.builder()
                .id(gameInventoryEntity.getId())
                .title(gameInventoryEntity.getTitle())
                .type(gameInventoryEntity.getType())
                .stock(gameInventoryEntity.getStock())
                .inventoryPriceModel(this.toModelGameInventoryPrice(gameInventoryEntity.getInventoryPriceEntity()))
                .build();
    }

    private GameInventoryPriceModel toModelGameInventoryPrice(GameInventoryPriceEntity gameInventoryPriceEntity) {
        return GameInventoryPriceModel.builder()
                .type(gameInventoryPriceEntity.getType())
                .amount(gameInventoryPriceEntity.getAmount())
                .currency(gameInventoryPriceEntity.getCurrency())
                .build();
    }


}

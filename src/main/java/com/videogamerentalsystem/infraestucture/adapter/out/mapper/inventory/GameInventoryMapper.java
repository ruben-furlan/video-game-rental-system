package com.videogamerentalsystem.infraestucture.adapter.out.mapper.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryPriceEntity;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


@Component
public class GameInventoryMapper {

    public Set<GameInventoryEntity> toEntityGameInventories(Set<GameInventoryModel> gameInventoryModel) {
        return CollectionUtils.isEmpty(gameInventoryModel) ? null : gameInventoryModel.stream().map(this::toEntityGameInventory).collect(Collectors.toSet());
    }

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

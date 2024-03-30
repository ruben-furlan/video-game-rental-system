package com.videogamerentalsystem.infraestucture.adapter.out.mapper.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.domain.model.inventory.constant.GamePriceType;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryPriceEntity;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameInventoryMapperTest {

    @InjectMocks
    private GameInventoryMapper gameInventoryMapper;

    @Test
    void case_001_toEntityGameInventory_ValidModel_ReturnsExpectedEntity() {
        // Given
        GameInventoryModel model = GameInventoryModel.builder()
                .title(TestGameInventoryHelper.TITLE_GAME_CLASSIC)
                .type(GameType.CLASSIC)
                .stock(10)
                .inventoryPriceModel(GameInventoryPriceModel.builder()
                        .type(GamePriceType.BASIC)
                        .amount(BigDecimal.TEN)
                        .currency(TestGameInventoryHelper.DEFAULT_CURRENCY)
                        .build())
                .build();

        // Act
        GameInventoryEntity entity = this.gameInventoryMapper.toEntityGameInventory(model);

        // Assert
        assertEquals(model.getTitle(), entity.getTitle());
        assertEquals(model.getType(), entity.getType());
        assertEquals(model.getStock(), entity.getStock());
        assertEquals(model.getInventoryPriceModel().getType(), entity.getInventoryPriceEntity().getType());
        assertEquals(model.getInventoryPriceModel().getAmount(), entity.getInventoryPriceEntity().getAmount());
        assertEquals(model.getInventoryPriceModel().getCurrency(), entity.getInventoryPriceEntity().getCurrency());
    }

    @Test
    void case_002_toModelGameInventory_ValidEntity_ReturnsExpectedModel() {
        // Given
        GameInventoryEntity entity = GameInventoryEntity.builder()
                .id(1L)
                .title(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE)
                .type(GameType.NEW_RELEASE)
                .stock(10)
                .inventoryPriceEntity(GameInventoryPriceEntity.builder()
                        .type(GamePriceType.PREMIUM)
                        .amount(BigDecimal.TEN)
                        .currency(TestGameInventoryHelper.DEFAULT_CURRENCY)
                        .build())
                .build();

        // Act
        GameInventoryModel model = this.gameInventoryMapper.toModelGameInventory(entity);

        // Assert
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getTitle(), model.getTitle());
        assertEquals(entity.getType(), model.getType());
        assertEquals(entity.getStock(), model.getStock());
        assertEquals(entity.getInventoryPriceEntity().getType(), model.getInventoryPriceModel().getType());
        assertEquals(entity.getInventoryPriceEntity().getAmount(), model.getInventoryPriceModel().getAmount());
        assertEquals(entity.getInventoryPriceEntity().getCurrency(), model.getInventoryPriceModel().getCurrency());
    }
}
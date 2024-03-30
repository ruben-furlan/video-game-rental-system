package com.videogamerentalsystem.infraestucture.adapter.out.mapper.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.domain.model.inventory.constant.GamePriceType;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.inventory.ResponseInventoryDTO;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class GameInventoryResponseApiMapperTest {

    @InjectMocks
    private GameInventoryResponseApiMapper gameInventoryResponseApiMapper;

    @Test
    void case_001_toResponseApi_ValidModel_ReturnsExpectedDTO() {
        // Given
        GameInventoryModel model = GameInventoryModel.builder()
                .id(1L)
                .title(TestGameInventoryHelper.TITLE_GAME_CLASSIC)
                .stock(10)
                .inventoryPriceModel(GameInventoryPriceModel.builder()
                        .type(GamePriceType.PREMIUM)
                        .amount(BigDecimal.TEN)
                        .currency(TestGameInventoryHelper.DEFAULT_CURRENCY)
                        .build())
                .build();

        // Act
        ResponseInventoryDTO dto = this.gameInventoryResponseApiMapper.toResponseApi(model);

        // Assert
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getTitle(), dto.getTitle());
        assertEquals(model.getStock(), dto.getStock());
        assertEquals(model.getInventoryPriceModel().getType(), dto.getPrice().getType());
        assertEquals(model.getInventoryPriceModel().getAmount(), dto.getPrice().getAmount());
        assertEquals(model.getInventoryPriceModel().getCurrency(), dto.getPrice().getCurrency());
    }

    @Test
    void case_002_toResponseApi_NullModel_ReturnsNullDTO() {
        // Act
        ResponseInventoryDTO dto = this.gameInventoryResponseApiMapper.toResponseApi(null);

        // Assert
        assertNull(dto);
    }
}
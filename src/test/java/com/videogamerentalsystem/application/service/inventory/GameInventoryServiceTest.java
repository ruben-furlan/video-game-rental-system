package com.videogamerentalsystem.application.service.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.constant.GamePriceType;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import com.videogamerentalsystem.domain.port.out.inventory.GameInventoryRepositoryPort;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiExceptionConstantsMessagesError;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


@ExtendWith(MockitoExtension.class)
class GameInventoryServiceTest {

    @Mock
    private GameInventoryRepositoryPort gameInventoryRepositoryPort;

    @InjectMocks
    private GameInventoryService gameInventoryService;


    @Test
    void case_001_createSuccessfullyCreated() {
        //Given
        GameInventoryCommand gameInventoryCommand = TestGameInventoryHelper.generateNewReleaseGameInventoryPriceDefaultCommand();
        GameInventoryModel gameInventoryModel = TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault();

        when(this.gameInventoryRepositoryPort.save(any())).thenReturn(gameInventoryModel);

        // Act
        GameInventoryModel createInventoryModel = this.gameInventoryService.create(gameInventoryCommand);

        // Assert
        assertNotNull(createInventoryModel);
        assertNotNull(createInventoryModel);
        assertEquals(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, createInventoryModel.getTitle());
        assertEquals(GameType.NEW_RELEASE, createInventoryModel.getType());
        assertEquals(10, createInventoryModel.getStock());
        assertNotNull(createInventoryModel.getInventoryPriceModel());
        assertEquals(GamePriceType.PREMIUM, createInventoryModel.getInventoryPriceModel().getType());
        assertEquals(TestGameInventoryHelper.DEFAULT_CURRENCY, createInventoryModel.getInventoryPriceModel().getCurrency());
        assertEquals(BigDecimal.valueOf(4), createInventoryModel.getInventoryPriceModel().getAmount());
    }

    @Test
    public void case_002_titleAlreadyExists() {
        // Given
        String existingTitle =TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE;
        GameInventoryCommand existingGameCommand =TestGameInventoryHelper.generateNewReleaseGameInventoryPriceDefaultCommand();

        // Act
        when(this.gameInventoryRepositoryPort.findByTitle(existingTitle)).thenReturn(Optional.of( GameInventoryModel.builder().build()));

        // Assert
        ApiException exception = assertThrows(ApiException.class, () -> this.gameInventoryService.create(existingGameCommand));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }


    @Test
    void case_003_findInventoryById_Found() {
        // Given
        Long existingId = 1L;
        GameInventoryModel existingInventory = TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault();
        when(this.gameInventoryRepositoryPort.findByID(existingId)).thenReturn(Optional.of(existingInventory));

        // Act
        Optional<GameInventoryModel> result = this.gameInventoryService.findInventoryById(existingId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(existingInventory, result.get());
    }

    @Test
    void case_004_findInventoryById_NotFound() {
        // Given
        Long nonExistingId = 2L;
        when(this.gameInventoryRepositoryPort.findByID(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Optional<GameInventoryModel> result = this.gameInventoryService.findInventoryById(nonExistingId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void case_005_findInventoryByTitle_Found() {
        // Given
        String existingTitle = TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE;
        GameInventoryModel existingInventory = TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault();
        when(this.gameInventoryRepositoryPort.findByTitle(existingTitle)).thenReturn(Optional.of(existingInventory));

        // Act
        Optional<GameInventoryModel> result = this.gameInventoryService.findInventoryByTitle(existingTitle);

        // Then
        assertTrue(result.isPresent());
        assertEquals(existingInventory, result.get());
    }

    @Test
    void case_006_findInventoryByTitle_NotFound() {
        // Given
        String nonExistingTitle = TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE;
        when(this.gameInventoryRepositoryPort.findByTitle(nonExistingTitle)).thenReturn(Optional.empty());

        // Act
        Optional<GameInventoryModel> result = this.gameInventoryService.findInventoryByTitle(nonExistingTitle);

        // Then
        assertFalse(result.isPresent());
    }


    @Test
    void case_007_stockExists_AllProductsHaveStock() {
        // Given
        List<String> titles = Arrays.asList(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, TestGameInventoryHelper.TITLE_GAME_STANDARD, TestGameInventoryHelper.TITLE_GAME_CLASSIC);
        List<GameInventoryModel> gameInventoryModels = Arrays.asList(
                TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault(),
                TestGameInventoryHelper.generateStandardGameInventoryModelDefault(),
        TestGameInventoryHelper.generateClassicGameInventoryModelDefault());

        // Act
        when(this.gameInventoryRepositoryPort.findByTitles(titles)).thenReturn(gameInventoryModels);

        // When & Then
        assertDoesNotThrow(() -> this.gameInventoryService.stockExists(titles));
    }

    @Test
    void case_008_stockExists_SomeProductsHaveNoStock() {
        // Given
        List<String> titles = Arrays.asList(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, TestGameInventoryHelper.TITLE_GAME_STANDARD, TestGameInventoryHelper.TITLE_GAME_CLASSIC);
        Integer stock = 0;
        List<GameInventoryModel> gameInventoryModels = Arrays.asList(
                TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault(),
                TestGameInventoryHelper.generateStandardGameInventoryModelDefault(),
                TestGameInventoryHelper.generateClassicGameInventory(stock));

        // Act
        when(this.gameInventoryRepositoryPort.findByTitles(titles)).thenReturn(gameInventoryModels);

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> this.gameInventoryService.stockExists(titles));
        assertEquals(ApiExceptionConstantsMessagesError.GameInventory.NOT_STOCK, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }


    @Test
    void case_009_stockRemove_ReduceStockForAllGameInventoryModels() {
        // Given
        List<GameInventoryModel> gameInventoryModels = Arrays.asList(
                TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault(),
                TestGameInventoryHelper.generateStandardGameInventoryModelDefault(),
                TestGameInventoryHelper.generateClassicGameInventoryModelDefault());

        // Act
        this.gameInventoryService.stockRemove(gameInventoryModels);

        // Then
        gameInventoryModels.forEach(gameInventoryModel -> {
            verify(this.gameInventoryRepositoryPort, atLeastOnce())
                    .updateStock(gameInventoryModel.getId(), gameInventoryModel.getStock() - 1);
        });
    }
    @Test
    void case_010_stockRemove_EmptyGameInventoryModelsList() {
        // Given
        List<GameInventoryModel> emptyGameInventoryModelsList = Arrays.asList();

        // Act
       this. gameInventoryService.stockRemove(emptyGameInventoryModelsList);

        // Then
        // No interactions with the repository should occur
        verifyNoInteractions(this.gameInventoryRepositoryPort);
    }
    @Test
    void case_011_stockAdd_IncreaseStockForGameInventoryModel() {
        // Given
        GameInventoryModel gameInventoryModel = TestGameInventoryHelper.generateClassicGameInventoryModelDefault();

        // Act
        this.gameInventoryService.stockAdd(gameInventoryModel);

        // Then
        verify(this.gameInventoryRepositoryPort, atLeastOnce()).updateStock(eq(gameInventoryModel.getId()), eq(gameInventoryModel.getStock() + 1));
    }
}
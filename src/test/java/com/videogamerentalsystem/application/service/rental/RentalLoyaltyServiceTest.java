package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalLoyalty;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.utils.DateFormatter;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import com.videogamerentalsystem.utils.TestRentalHelper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



public class RentalLoyaltyServiceTest {

    @Test
    void case_001_calculateTotalPoints_EmptyProductModels_ReturnsZero() {
        // Arrange
        List<RentalProductModel> productModels = Arrays.asList();

        // Act
        RentalLoyaltyService rentalLoyaltyService = new RentalLoyaltyService();
        Integer totalPoints = rentalLoyaltyService.calculateTotalPoints(productModels);

        // Assert
        assertEquals(0, totalPoints);
    }

    @Test
    void case_002_calculateTotalPoints_ProductModelsWithDifferentTypes_ReturnsCorrectTotalPoints() {
        // Arrange
        List<RentalProductModel> productModels  =  Arrays.asList(
                TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, GameType.NEW_RELEASE, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(20.00)),
                TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_STANDARD, GameType.STANDARD, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_CLASSIC, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_CLASSIC_2, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(3.00))
        );

        // Act
        RentalLoyaltyService rentalLoyaltyService = new RentalLoyaltyService();
        Integer totalPoints = rentalLoyaltyService.calculateTotalPoints(productModels);

        // Assert
        assertEquals(RentalLoyalty.NEW_RELEASE_POINT.getValue() + RentalLoyalty.STANDARD_POINT.getValue() +
                RentalLoyalty.CLASSIC_POINT.getValue() + RentalLoyalty.CLASSIC_POINT.getValue(), totalPoints);
    }

    @Test
    void case_003_calculateTotalPoints_ProductModelsWithSameType_ReturnsCorrectTotalPoints() {
        // Arrange
        List<RentalProductModel> productModels  =  Arrays.asList(
                TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_CLASSIC, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_CLASSIC, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_CLASSIC_2, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(3.00))
        );


        // Act
        RentalLoyaltyService rentalLoyaltyService = new RentalLoyaltyService();
        Integer totalPoints = rentalLoyaltyService.calculateTotalPoints(productModels);

        // Assert
        assertEquals(RentalLoyalty.CLASSIC_POINT.getValue() * 3, totalPoints);
    }

}
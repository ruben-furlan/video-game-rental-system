package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiExceptionConstantsMessagesError;
import com.videogamerentalsystem.utils.DateFormatter;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import com.videogamerentalsystem.utils.TestRentalHelper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentalPaymentCalculationServiceTest {

    @InjectMocks
    private RentalPaymentCalculationService rentalPaymentCalculationService;

    @Test
    void case_001_applyAndCalculateRentalCost_Success() {
        // Given
        RentalModel rentalModel = TestRentalHelper.generateRentalDefaultMode();
        List<GameInventoryModel> gameInventoryModels = Arrays.asList(
                TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault(),
                TestGameInventoryHelper.generateStandardGameInventoryModelDefault(),
                TestGameInventoryHelper.generateClassicGameInventoryModelDefault());

        // Act
        this.rentalPaymentCalculationService.applyAndCalculateRentalCost(rentalModel, gameInventoryModels);

        // Assert
        assertEquals(RentalProductStatus.IN_PROGRESS, rentalModel.getProductModels().get(0).getStatus());
        assertEquals(RentalProductStatus.IN_PROGRESS, rentalModel.getProductModels().get(1).getStatus());
        assertEquals(RentalProductStatus.IN_PROGRESS, rentalModel.getProductModels().get(2).getStatus());
        assertEquals(RentalProductStatus.IN_PROGRESS, rentalModel.getProductModels().get(3).getStatus());
    }

    @Test
    void case_002_applyAndCalculateRentalCost_EmptyRentalModel_ExceptionThrown() {
        // Given
        RentalModel rentalModel = RentalModel.builder().productModels(Collections.emptyList()).build();
        List<GameInventoryModel> gameInventoryModels = Collections.emptyList();

        // Act and Assert
        ApiException exception = assertThrows(ApiException.class, () -> this.rentalPaymentCalculationService.applyAndCalculateRentalCost(rentalModel, gameInventoryModels));
        assertEquals(ApiExceptionConstantsMessagesError.Generic.MESSAGE_GENERIC, exception.getMessage());
    }

    @Test
    void case_002_applySurchargeForProduct_ExtraDays_OneExtraDay_Success() {
        // Given
        RentalProductModel productModel = TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS,
                TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, GameType.NEW_RELEASE, LocalDateTime.now().minusDays(1), BigDecimal.valueOf(4));

        // Act
        this.rentalPaymentCalculationService.applySurchargeForProduct(productModel);

        // Assert
        assertEquals(BigDecimal.valueOf(8), productModel.getCharges().getTotal());
        assertNotNull(productModel.getCharges().getSurcharges());
        assertEquals("EXTRAS_DAYS: 1", productModel.getCharges().getSurcharges().getReason());
    }

    @Test
    void case_004_applySurchargeForProduct_NoExtraDays_NoSurcharge() {
        // Arrange
        RentalProductModel productModel = TestRentalHelper.generateRentalProductGame(RentalProductStatus.IN_PROGRESS,
                TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, GameType.NEW_RELEASE, LocalDateTime.now().plusDays(1), BigDecimal.valueOf(4));

        // Act
        this.rentalPaymentCalculationService.applySurchargeForProduct(productModel);

        // Assert
        assertEquals(BigDecimal.valueOf(4), productModel.getCharges().getPrice());
    }
}

package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.application.service.inventory.GameInventoryService;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
import com.videogamerentalsystem.domain.port.out.rental.RentalRepositoryPort;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiExceptionConstantsMessagesError;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import com.videogamerentalsystem.utils.TestRentalHelper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
    @Mock
    private RentalRepositoryPort rentalRepositoryPort;
    @Mock
    private GameInventoryService gameInventoryService;
    @Mock
    private  RentalPaymentCalculationService rentalPaymentCalculationService;

    @Mock
    private RentalLoyaltyService loyaltyService;

    @InjectMocks
    private RentalService rentalService;


    @Test
    void case_0001_create_success() {
        // Given
        RentalCommand rentalCommand = TestRentalHelper.generateRentalDefaultCommand();
        RentalModel rentalModel = TestRentalHelper.generateRentalDefaultMode();

        List<GameInventoryModel> gameInventoryModels = Arrays.asList(
                TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault(),
                TestGameInventoryHelper.generateStandardGameInventoryModelDefault(),
                TestGameInventoryHelper.generateClassicGameInventoryModelDefault());


        when(this.gameInventoryService.stockExists(anyList())).thenReturn(gameInventoryModels);
        when(this.rentalRepositoryPort.create(any())).thenReturn(rentalModel);

        RentalModel createdRentalModel = this.rentalService.create(rentalCommand);

        assertNotNull(createdRentalModel);
        // Then
        assertNotNull(createdRentalModel, "The created rental model is null");

        // Verify that the ID of the returned model is the same as the expected model's ID
        assertEquals(rentalModel.getId(), createdRentalModel.getId(), "IDs of models do not match");

        // Verify that the date of the returned model is the same as the expected model's date
        assertEquals(rentalModel.getDate(), createdRentalModel.getDate(), "Dates of models do not match");

        // Verify that the currency type of the returned model is the same as the expected model's currency type
        assertEquals(rentalModel.getCurrency(), createdRentalModel.getCurrency(), "Currencies of models do not match");

        // Verify that the payment type of the returned model is the same as the expected model's payment type
        assertEquals(rentalModel.getPaymentType(), createdRentalModel.getPaymentType(), "Payment types of models do not match");

        // Verify that the customer of the returned model is the same as the expected model's customer
        assertEquals(rentalModel.getCustomerModel(), createdRentalModel.getCustomerModel(), "Customers of models do not match");

        // Verify that the products of the returned model are the same as the expected model's products
        assertEquals(rentalModel.getProductModels(), createdRentalModel.getProductModels(), "Products of models do not match");
    }

    @Test
    void case_0002_create_no_stock_available() {
        // Given
        RentalCommand rentalCommand = TestRentalHelper.generateRentalDefaultCommand();
        when(this.gameInventoryService.stockExists(anyList()))
                .thenThrow(new ApiException(ApiExceptionConstantsMessagesError.NOT_STOCK, HttpStatus.BAD_REQUEST));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> rentalService.create(rentalCommand));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus(), "HTTP status should be BAD_REQUEST");
    }

    @Test
    void case_0003_create_duplicated_rental_titles() {
        // Given
        RentalCommand rentalCommand = TestRentalHelper.generateRentalCommandDuplicateProduct(TestGameInventoryHelper.DEFAULT_CURRENCY,RentalPaymentType.CREDIT);

        // When
        ApiException exception = assertThrows(ApiException.class, () -> this.rentalService.create(rentalCommand));

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus(), "HTTP status should be BAD_REQUEST");
        assertTrue(exception.getMessage().contains("Duplicated titles found"), "Exception message should contain 'Duplicated titles found'");
    }


    @Test
    void case_004_handBackGame_success() {
        // Given
        Long rentalId = 1L;
        Long rentalProductId = 1L;

        RentalModel rentalModel = TestRentalHelper.generateRentalDefaultMode();
        GameInventoryModel gameInventoryModel = TestGameInventoryHelper.generateNewReleaseGameInventoryModelDefault();

        // Mocking
        when(this.rentalRepositoryPort.findRentalById(rentalId)).thenReturn(Optional.of(rentalModel));
        when(this.gameInventoryService.findInventoryByTitle(any(String.class))).thenReturn(Optional.of(gameInventoryModel));
        Mockito.doNothing().when(this.rentalPaymentCalculationService).applySurchargeForProduct(any());

        // When
        RentalModel result = this.rentalService.handBackGame(rentalId, rentalProductId);

        // Then
        assertNotNull(result);
        assertEquals(rentalModel, result);
}

    @Test
    void case_005_handBackGame_rental_not_found() {
        // Given
        Long rentalId = 1L;
        Long rentalProductId = 1L;
        when(this.rentalRepositoryPort.findRentalById(rentalId)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> rentalService.handBackGame(rentalId, rentalProductId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus(), "HTTP status should be NOT_FOUND");
    }

    @Test
    void case_006_handBackGame_product_already_finished() {
        // Given
        Long rentalId = 1L;
        Long rentalProductId = 1L;
        RentalModel rentalModel = TestRentalHelper.generateRentalDefaultModeProductFinish();
        when(this.rentalRepositoryPort.findRentalById(rentalId)).thenReturn(Optional.of(rentalModel));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> this.rentalService.handBackGame(rentalId, rentalProductId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus(), "HTTP status should be BAD_REQUEST");
    }

    @Test
    void case_007_get_rental_found() {
        // Given
        Long rentalId = 1L;
        RentalModel expectedRentalModel = TestRentalHelper.generateRentalDefaultMode();
        when(this.rentalRepositoryPort.findRentalById(rentalId)).thenReturn(Optional.of(expectedRentalModel));

        // When
        RentalModel retrievedRentalModel = this.rentalService.get(rentalId);

        // Then
        assertNotNull(retrievedRentalModel);
        assertEquals(expectedRentalModel, retrievedRentalModel, "Retrieved rental model should match the expected rental model");
    }

    @Test
    void case_008_get_rental_not_found() {
        // Given
        Long rentalId = 1L;
        when(this.rentalRepositoryPort.findRentalById(rentalId)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> this.rentalService.get(rentalId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus(), "HTTP status should be NOT_FOUND");
    }
}
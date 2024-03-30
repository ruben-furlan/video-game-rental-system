package com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalCustomerEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalProductEntity;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import com.videogamerentalsystem.utils.TestRentalHelper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class RentalMapperTest {

    @InjectMocks
    private RentalMapper rentalMapper;

    @Test
    void case_001_toRentalEntity_ValidModel_ReturnsExpectedEntity() {
        // Given
        RentalCustomerModel customerModel = RentalCustomerModel.builder()
                .firstName(TestRentalHelper.Customer.FIRST_NAME)
                .latName(TestRentalHelper.Customer.LAST_NAME)
                .documentNumber(TestRentalHelper.Customer.DOCUMENT_NUMBER)
                .loyaltyPoints(10)
                .build();

        RentalProductModel productModel1 = RentalProductModel.builder()
                .title(TestGameInventoryHelper.TITLE_GAME_CLASSIC_2)
                .status(RentalProductStatus.IN_PROGRESS)
                .type(GameType.NEW_RELEASE)
                .endDate(LocalDateTime.now().plusDays(7))
                .charges(RentalProductChargeModel.builder().price(BigDecimal.TEN).build())
                .build();

        RentalProductModel productModel2 = RentalProductModel.builder()
                .title(TestGameInventoryHelper.TITLE_GAME_CLASSIC)
                .status(RentalProductStatus.IN_PROGRESS)
                .type(GameType.STANDARD)
                .endDate(LocalDateTime.now().plusDays(5))
                .charges(RentalProductChargeModel.builder().price(BigDecimal.valueOf(5)).build())
                .build();

        List<RentalProductModel> productModels = Arrays.asList(productModel1, productModel2);

        RentalModel rentalModel = RentalModel.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .currency(TestGameInventoryHelper.DEFAULT_CURRENCY)
                .paymentType(RentalPaymentType.CREDIT)
                .customerModel(customerModel)
                .productModels(productModels)
                .build();

        // Act
        RentalEntity rentalEntity = this.rentalMapper.toRentalEntity(rentalModel);

        // Assert
        assertNotNull(rentalEntity);
        assertEquals(rentalModel.getDate(), rentalEntity.getDate());
        assertEquals(rentalModel.getCurrency(), rentalEntity.getCurrency());
        assertEquals(rentalModel.getPaymentType(), rentalEntity.getPaymentType());
        assertNotNull(rentalEntity.getCustomer());
        assertEquals(customerModel.getFirstName(), rentalEntity.getCustomer().getFirstName());
        assertEquals(customerModel.getLatName(), rentalEntity.getCustomer().getLatName());
        assertEquals(customerModel.getDocumentNumber(), rentalEntity.getCustomer().getDocumentNumber());
        assertEquals(customerModel.getLoyaltyPoints(), rentalEntity.getCustomer().getLoyaltyPoints());
        assertEquals(2, rentalEntity.getRentalProducts().size());
        assertEquals(productModel1.getTitle(), rentalEntity.getRentalProducts().get(0).getTitle());
        assertEquals(productModel2.getTitle(), rentalEntity.getRentalProducts().get(1).getTitle());
    }

    @Test
    void case_002_toRentalModel_ValidEntity_ReturnsExpectedModel() {
        // Given
        RentalCustomerEntity customerEntity = RentalCustomerEntity.builder()
                .firstName(TestRentalHelper.Customer.FIRST_NAME)
                .latName(TestRentalHelper.Customer.LAST_NAME)
                .documentNumber(TestRentalHelper.Customer.DOCUMENT_NUMBER)
                .loyaltyPoints(5)
                .build();

        RentalProductEntity productEntity1 = RentalProductEntity.builder()
                .title(TestGameInventoryHelper.TITLE_GAME_CLASSIC)
                .status(RentalProductStatus.IN_PROGRESS)
                .type(GameType.CLASSIC)
                .endDate(LocalDateTime.now().plusDays(3))
                .price(BigDecimal.valueOf(7))
                .build();

        RentalProductEntity productEntity2 = RentalProductEntity.builder()
                .title(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE)
                .status(RentalProductStatus.IN_PROGRESS)
                .type(GameType.NEW_RELEASE)
                .endDate(LocalDateTime.now().plusDays(10))
                .price(BigDecimal.valueOf(15))
                .build();

        List<RentalProductEntity> productEntities = Arrays.asList(productEntity1, productEntity2);

        RentalEntity rentalEntity = RentalEntity.builder()
                .id(2L)
                .date(LocalDateTime.now().minusDays(1))
                .currency(TestGameInventoryHelper.DEFAULT_CURRENCY)
                .paymentType(RentalPaymentType.CREDIT)
                .customer(customerEntity)
                .rentalProducts(productEntities)
                .build();

        // Act
        RentalModel rentalModel = rentalMapper.toRentalModel(rentalEntity);

        // Assert
        assertNotNull(rentalModel);
        assertEquals(rentalEntity.getId(), rentalModel.getId());
        assertEquals(rentalEntity.getDate(), rentalModel.getDate());
        assertEquals(rentalEntity.getCurrency(), rentalModel.getCurrency());
        assertEquals(rentalEntity.getPaymentType(), rentalModel.getPaymentType());
        assertNotNull(rentalModel.getCustomerModel());
        assertEquals(customerEntity.getFirstName(), rentalModel.getCustomerModel().getFirstName());
        assertEquals(customerEntity.getLatName(), rentalModel.getCustomerModel().getLatName());
        assertEquals(customerEntity.getDocumentNumber(), rentalModel.getCustomerModel().getDocumentNumber());
        assertEquals(customerEntity.getLoyaltyPoints(), rentalModel.getCustomerModel().getLoyaltyPoints());
        assertEquals(2, rentalModel.getProductModels().size());
        assertEquals(productEntity1.getTitle(), rentalModel.getProductModels().get(0).getTitle());
        assertEquals(productEntity2.getTitle(), rentalModel.getProductModels().get(1).getTitle());
    }
}
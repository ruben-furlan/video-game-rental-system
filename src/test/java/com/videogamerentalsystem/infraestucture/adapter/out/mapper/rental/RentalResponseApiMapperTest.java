package com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductChargeDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductSurchargesDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseRentalCustomerDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseRentalDTO;
import com.videogamerentalsystem.utils.TestGameInventoryHelper;
import com.videogamerentalsystem.utils.TestRentalHelper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentalResponseApiMapperTest {

    @InjectMocks
    private RentalResponseApiMapper rentalResponseApiMapper;

    @Test
    void case_001_toResponseApi_ValidModel_ReturnsExpectedResponse() {
        // Given
        RentalCustomerModel customerModel = RentalCustomerModel.builder()
                .firstName(TestRentalHelper.Customer.FIRST_NAME)
                .latName(TestRentalHelper.Customer.LAST_NAME)
                .documentNumber(TestRentalHelper.Customer.DOCUMENT_NUMBER)
                .loyaltyPoints(10)
                .build();

        RentalProductModel productModel1 = RentalProductModel.builder()
                .id(1L)
                .title(TestGameInventoryHelper.TITLE_GAME_CLASSIC_2)
                .status(RentalProductStatus.IN_PROGRESS)
                .type(GameType.NEW_RELEASE)
                .numberDays(7)
                .endDate(LocalDateTime.now().plusDays(7))
                .charges(RentalProductChargeModel.builder()
                        .price(BigDecimal.TEN)
                        .total(BigDecimal.valueOf(11))
                        .build())
                .build();

        RentalProductModel productModel2 = RentalProductModel.builder()
                .id(2L)
                .title(TestGameInventoryHelper.TITLE_GAME_CLASSIC)
                .status(RentalProductStatus.IN_PROGRESS)
                .type(GameType.STANDARD)
                .numberDays(5)
                .endDate(LocalDateTime.now().plusDays(5))
                .charges(RentalProductChargeModel.builder()
                        .price(BigDecimal.valueOf(5))
                        .total(BigDecimal.valueOf(5))
                        .build())
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
        ResponseRentalDTO response = this.rentalResponseApiMapper.toResponseApi(rentalModel);

        // Assert
        assertNotNull(response);
        assertEquals(rentalModel.getId(), response.getId());
        assertEquals(rentalModel.getDate(), response.getDate());
        assertEquals(rentalModel.getCurrency(), response.getCurrency());
        assertEquals(rentalModel.getPaymentType(), response.getPaymentType());

        ResponseRentalCustomerDTO customerDTO = response.getCustomer();
        assertNotNull(customerDTO);
        assertEquals(customerModel.getFirstName(), customerDTO.getFirstName());
        assertEquals(customerModel.getLatName(), customerDTO.getLatName());
        assertEquals(customerModel.getDocumentNumber(), customerDTO.getDocumentNumber());
        assertEquals(customerModel.getLoyaltyPoints(), customerDTO.getTotalLoyaltyPointsGenerated());

        List<ResponseProductDTO> productDTOs = response.getProducts();
        assertNotNull(productDTOs);
        assertEquals(productModels.size(), productDTOs.size());

        ResponseProductDTO productDTO1 = productDTOs.get(0);
        assertNotNull(productDTO1);
        assertEquals(productModel1.getId(), productDTO1.getId());
        assertEquals(productModel1.getTitle(), productDTO1.getTitle());
        assertEquals(productModel1.getStatus(), productDTO1.getStatus());
        assertEquals(productModel1.getType(), productDTO1.getType());
        assertEquals(productModel1.getNumberDays(), productDTO1.getNumberDays());
        assertEquals(productModel1.getEndDate(), productDTO1.getEndDate());

        ResponseProductChargeDTO chargeDTO1 = productDTO1.getCharges();
        assertNotNull(chargeDTO1);
        assertEquals(productModel1.getCharges().getPrice(), chargeDTO1.getPrice());


        ResponseProductDTO productDTO2 = productDTOs.get(1);
        assertNotNull(productDTO2);
        assertEquals(productModel2.getId(), productDTO2.getId());
        assertEquals(productModel2.getTitle(), productDTO2.getTitle());
        assertEquals(productModel2.getStatus(), productDTO2.getStatus());
        assertEquals(productModel2.getType(), productDTO2.getType());
        assertEquals(productModel2.getNumberDays(), productDTO2.getNumberDays());
        assertEquals(productModel2.getEndDate(), productDTO2.getEndDate());

        ResponseProductChargeDTO chargeDTO2 = productDTO2.getCharges();
        assertNotNull(chargeDTO2);
        assertEquals(productModel2.getCharges().getPrice(), chargeDTO2.getPrice());
        assertEquals(productModel2.getCharges().getTotal(), chargeDTO2.getTotal());
    }
}
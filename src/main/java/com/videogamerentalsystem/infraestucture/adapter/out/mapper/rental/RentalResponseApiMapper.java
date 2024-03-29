package com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental;

import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductSurchargeModel;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductChargeDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductSurchargesDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseRentalCustomerDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseRentalDTO;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RentalResponseApiMapper {

    public ResponseRentalDTO toResponseApi(RentalModel rentalModel) {
        return ResponseRentalDTO.builder()
                .id(rentalModel.getId())
                .date(rentalModel.getDate())
                .currency(rentalModel.getCurrency())
                .paymentType(rentalModel.getPaymentType())
                .customer(this.toResponseRentalCustomerApi(rentalModel.getCustomerModel()))
                .products(this.toResponseProductsApi(rentalModel.getProductModels()))
                .build();
    }

    private ResponseRentalCustomerDTO toResponseRentalCustomerApi(RentalCustomerModel customerModel) {
        return ResponseRentalCustomerDTO.builder()
                .firstName(customerModel.getFirstName())
                .latName(customerModel.getLatName())
                .documentNumber(customerModel.getDocumentNumber())
                .totalLoyaltyPointsGenerated(customerModel.getLoyaltyPoints())
                .build();
    }

    private Set<ResponseProductDTO> toResponseProductsApi(Set<RentalProductModel> productModels) {
        return productModels.stream().map(this::toResponseProductApi).collect(Collectors.toSet());
    }

    private ResponseProductDTO toResponseProductApi(RentalProductModel rentalProductModel) {
        return ResponseProductDTO.builder()
                .id(rentalProductModel.getId())
                .title(rentalProductModel.getTitle())
                .type(rentalProductModel.getType())
                .numberDays(rentalProductModel.getNumberDays())
                .endDate(rentalProductModel.getEndDate())
                .charges(this.toResponseProductChargeApi(rentalProductModel.getCharges()))
                .build();
    }

    private ResponseProductChargeDTO toResponseProductChargeApi(RentalProductChargeModel chargeModel) {
        return ResponseProductChargeDTO.builder()
                .price(chargeModel.getPrice())
                .surcharges(this.toResponseProductSurchargeApi(chargeModel.getSurcharges()))
                .total(chargeModel.getTotal())
                .build();
    }

    private ResponseProductSurchargesDTO toResponseProductSurchargeApi(RentalProductSurchargeModel surchargesModel) {
        return Objects.isNull(surchargesModel)?null:ResponseProductSurchargesDTO.builder()
                .amount(surchargesModel.getAmount())
                .reason(surchargesModel.getReason())
                .build();
    }

}

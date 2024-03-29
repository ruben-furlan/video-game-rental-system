package com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental;

import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductChargeDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseProductSurchargesDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseRentalCustomerDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseRentalDTO;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RentalResponseApiMapper {


    public ResponseRentalDTO toResponseApi(RentalModel rentalModel) {

        RentalCustomerModel customerModel = rentalModel.getCustomerModel();

        ResponseRentalCustomerDTO customerDTO = ResponseRentalCustomerDTO.builder()
                .firstName(customerModel.getFirstName())
                .latName(customerModel.getLatName())
                .loyaltyPoints(customerModel.getLoyaltyPoints())
                .build();


        Set<RentalProductModel> productModels = rentalModel.getProductModels();

        Set<ResponseProductDTO> productDTOS = productModels.stream().map(p -> {
            ResponseProductSurchargesDTO surchages = ResponseProductSurchargesDTO.builder().amount(BigDecimal.valueOf(100))
                    .reason("hola").build();

            ResponseProductChargeDTO charges = ResponseProductChargeDTO.builder()
                    .price(p.getPrice())
                    .surcharges(surchages)
                    .total(p.getPrice().add(surchages.getAmount())).build();


            return ResponseProductDTO.builder()
                    .id(p.getId())
                    .title(p.getTitle())
                    .type(p.getType())
                    .numberDate(1)
                    .endDate(p.getEndDate())
                    .charges(charges).build();
        }).collect(Collectors.toSet());


        return ResponseRentalDTO.builder()
                .id(rentalModel.getId())
                .date(rentalModel.getDate())
                .currency(rentalModel.getCurrency())
                .paymentType(rentalModel.getPaymentType())
                .customer(customerDTO)
                .products(productDTOS)
                .build();
    }


}

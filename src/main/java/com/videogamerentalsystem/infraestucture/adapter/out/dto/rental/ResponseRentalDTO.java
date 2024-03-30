package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseRentalDTO {
    private Long id;
    private LocalDateTime date;
    private String currency;
    private RentalPaymentType paymentType;
    private ResponseRentalCustomerDTO customer;
    private List<ResponseProductDTO> products;;
}

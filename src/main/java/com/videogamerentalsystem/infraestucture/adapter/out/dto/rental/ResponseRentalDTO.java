package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import java.time.LocalDateTime;
import java.util.Set;
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
    private Set<ResponseProductDTO> products;;
}

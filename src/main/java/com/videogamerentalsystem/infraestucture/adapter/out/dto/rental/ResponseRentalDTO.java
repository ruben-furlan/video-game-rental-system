package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseRentalDTO {
    private Long id;
    private LocalDateTime date;
    private Integer numberDate;
    private String currency;
    private String paymentType;
    private ResponseRentalCustomerDTO customer;
    private Set<ResponseProductDTO> products;;
}

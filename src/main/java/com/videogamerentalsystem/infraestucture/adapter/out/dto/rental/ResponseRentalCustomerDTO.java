package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseRentalCustomerDTO {
    private String firstName;
    private String latName;
    private Integer loyaltyPoints;
}

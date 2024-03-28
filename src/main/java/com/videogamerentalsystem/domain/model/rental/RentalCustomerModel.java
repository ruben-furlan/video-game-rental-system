package com.videogamerentalsystem.domain.model.rental;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalCustomerModel {
    private String firstName;
    private String latName;
    private Integer loyaltyPoints;
}

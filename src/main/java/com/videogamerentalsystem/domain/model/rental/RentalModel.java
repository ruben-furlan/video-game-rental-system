package com.videogamerentalsystem.domain.model.rental;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalModel {
    private LocalDateTime date;
    private String currency;
    private String paymentType;
    private RentalCustomerModel customerModel;
    private Set<RentalProductModel> productModels;
}

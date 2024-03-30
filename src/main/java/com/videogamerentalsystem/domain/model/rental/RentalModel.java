package com.videogamerentalsystem.domain.model.rental;

import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder
@Data
@Accessors(chain = true)
public class RentalModel {
    private Long id;
    private LocalDateTime date;
    private String currency;
    private RentalPaymentType paymentType;
    private RentalCustomerModel customerModel;
    private List<RentalProductModel> productModels;
}

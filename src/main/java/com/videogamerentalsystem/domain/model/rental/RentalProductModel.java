package com.videogamerentalsystem.domain.model.rental;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RentalProductModel {

    private Long id;
    private RentalProductStatus status;

    private String title;
    private GameType type;
    private Integer numberDays;

    private LocalDateTime endDate;
    private RentalProductChargeModel charges;

    public void updateCharges(RentalProductChargeModel charges) {
        this.charges = charges;
    }
    public void updateType(GameType type) {
        this.type = type;
    }
    public void updateNumberDays(Integer numberDays) {
        this.numberDays = numberDays;
    }

    public void updateStatus(RentalProductStatus status){
        this.status = status;
    }
}

package com.videogamerentalsystem.domain.model.rental;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Builder
@Setter
public class RentalProductModel {
    private Long id;
    private String title;
    private String type;
    private Integer numberDays;
    private LocalDateTime endDate;
    private RentalProductChargeModel charges;
}

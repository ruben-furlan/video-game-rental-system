package com.videogamerentalsystem.domain.model.rental;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalProductModel {
    private Long id;
    private String title;
    private String type;
    private Integer numberDate;
    private LocalDateTime endDate;
    private RentalProductChargeModel charges;
}

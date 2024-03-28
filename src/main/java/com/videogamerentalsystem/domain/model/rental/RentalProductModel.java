package com.videogamerentalsystem.domain.model.rental;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalProductModel {
    private String title;
    private String type;
    private LocalDateTime endDate;
    private BigDecimal price;
}

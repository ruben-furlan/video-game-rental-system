package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseProductDTO {
    private Long id;
    private String title;
    private String type;
    private Integer numberDate;
    private LocalDateTime endDate;
    private ResponseProductChargeDTO charges;
}

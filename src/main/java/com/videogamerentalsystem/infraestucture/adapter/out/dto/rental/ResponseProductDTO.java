package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseProductDTO {
    private Long id;
    private String title;
    private GameType type;
    private Integer numberDays;
    private LocalDateTime endDate;
    private ResponseProductChargeDTO charges;
}

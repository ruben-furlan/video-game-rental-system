package com.videogamerentalsystem.infraestucture.adapter.out.dto.rental;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseProductSurchargesDTO {
    private String reason;
    private BigDecimal amount;
}

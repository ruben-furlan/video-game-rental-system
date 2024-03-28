package com.videogamerentalsystem.infraestucture.adapter.out.dto.inventory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseInventoryDTO {
    private Long id;
    private String title;
    private String type;
    private Integer stock;
    private ResponseInventoryPriceDTO price;
}

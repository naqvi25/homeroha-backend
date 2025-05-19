package com.homeroha.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryItemResponseDTO {
    private Long id;
    private String name;
    private Integer quantity;
    private String unit;
    private Integer threshold;
    private String category;
}

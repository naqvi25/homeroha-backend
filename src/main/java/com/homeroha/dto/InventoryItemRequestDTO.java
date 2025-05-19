package com.homeroha.dto;

import lombok.Data;

@Data
public class InventoryItemRequestDTO {
    private String name;
    private Integer quantity;
    private String unit;
    private Integer threshold;
    private String category;
    private Long homeId; // as we will be needing inventory item for a particular home
}

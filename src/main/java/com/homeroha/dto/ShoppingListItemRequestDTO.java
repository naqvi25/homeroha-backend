package com.homeroha.dto;

import lombok.Data;

@Data
public class ShoppingListItemRequestDTO {
    private String name;
    private Integer quantity;
    private String unit;
    private String category; // ✅ ADD THIS
    private Long homeId; // ✅ optional
}


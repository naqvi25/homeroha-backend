package com.homeroha.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true) // ✅ Important
public class ShoppingListItemResponseDTO {
    private Long id;
    private String name;
    private Integer quantity;
    private String unit;
    private String category; // ✅ ADD this
    private boolean bought;
    private String createdBy;
    private LocalDateTime createdAt;
}


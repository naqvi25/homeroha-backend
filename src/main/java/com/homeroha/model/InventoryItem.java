package com.homeroha.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;
    private String unit;       // e.g., kg, L, pcs
    private Integer threshold; // for low-stock alert
    private String category;   // optional

    // Link back to the ShoppingListItem that created this entry
    @Column(name = "shopping_list_item_id")
    private Long shoppingListItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;
}
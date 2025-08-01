package com.homeroha.service;

import com.homeroha.dto.ShoppingListItemRequestDTO;
import com.homeroha.dto.ShoppingListItemResponseDTO;

import java.util.List;

public interface ShoppingListService {
    List<ShoppingListItemResponseDTO> getItems(String userEmail, Long homeId);
    List<ShoppingListItemResponseDTO> getItems(String userEmail);
    ShoppingListItemResponseDTO addItem(String userEmail, ShoppingListItemRequestDTO request);
    ShoppingListItemResponseDTO markBought(Long id, String userEmail);

    // NEW: unmark functionality
    ShoppingListItemResponseDTO unmarkBought(Long id, String userEmail);

    void deleteItem(Long id, String userEmail);

    // NEW: update item
    ShoppingListItemResponseDTO updateItem(Long id, String userEmail, ShoppingListItemRequestDTO request);
}
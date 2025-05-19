package com.homeroha.service;

import com.homeroha.dto.InventoryItemRequestDTO;
import com.homeroha.dto.InventoryItemResponseDTO;
import java.util.List;

public interface InventoryService {
    InventoryItemResponseDTO addItem(String userEmail, InventoryItemRequestDTO request);
    List<InventoryItemResponseDTO> getAllItems(String userEmail, Long homeId); // 🔥 Changed
    List<InventoryItemResponseDTO> getLowStockItems(String userEmail, Long homeId); // 🔥 Changed
    InventoryItemResponseDTO updateItem(Long id, String userEmail, InventoryItemRequestDTO request);
    void deleteItem(Long id, String userEmail);
}

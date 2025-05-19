package com.homeroha.controller;

import com.homeroha.dto.InventoryItemRequestDTO;
import com.homeroha.dto.InventoryItemResponseDTO;
import com.homeroha.exception.HomerohaException;
import com.homeroha.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody InventoryItemRequestDTO request, Principal principal) {
        try {
            InventoryItemResponseDTO response = inventoryService.addItem(principal.getName(), request); // ✅
            return ResponseEntity.ok(response); // ✅
        } catch (HomerohaException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getInventory(@RequestParam(value = "homeId", required = false) Long homeId, Principal principal) {
        return ResponseEntity.ok(inventoryService.getAllItems(principal.getName(), homeId)); // ✅ Pass homeId
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockItems(@RequestParam(value = "homeId", required = false) Long homeId, Principal principal) {
        return ResponseEntity.ok(inventoryService.getLowStockItems(principal.getName(), homeId)); // ✅ Pass homeId
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody InventoryItemRequestDTO request, Principal principal) {
        try {
            InventoryItemResponseDTO response = inventoryService.updateItem(id, principal.getName(), request);
            return ResponseEntity.ok(response);
        } catch (HomerohaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id, Principal principal) {
        try {
            inventoryService.deleteItem(id, principal.getName());
            return ResponseEntity.ok(Map.of("message", "Item deleted successfully", "itemId", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

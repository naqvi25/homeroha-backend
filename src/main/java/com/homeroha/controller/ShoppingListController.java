package com.homeroha.controller;

import com.homeroha.dto.ShoppingListItemRequestDTO;
import com.homeroha.dto.ShoppingListItemResponseDTO;
import com.homeroha.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shopping-list")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @GetMapping
    public ResponseEntity<List<ShoppingListItemResponseDTO>> listItems(
            @RequestParam(value = "homeId", required = false) Long homeId,
            Principal principal
    ) {
        List<ShoppingListItemResponseDTO> items = shoppingListService.getItems(principal.getName(), homeId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/add")
    public ResponseEntity<ShoppingListItemResponseDTO> addItem(
            @RequestBody ShoppingListItemRequestDTO request,
            Principal principal
    ) {
        ShoppingListItemResponseDTO created = shoppingListService.addItem(principal.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/mark-bought/{id}")
    public ResponseEntity<ShoppingListItemResponseDTO> markBought(
            @PathVariable Long id,
            Principal principal
    ) {
        ShoppingListItemResponseDTO response = shoppingListService.markBought(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/unmark-bought/{id}")
    public ResponseEntity<ShoppingListItemResponseDTO> unmarkBought(
            @PathVariable Long id,
            Principal principal
    ) {
        ShoppingListItemResponseDTO response = shoppingListService.unmarkBought(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    // NEW: update item endpoint
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateItem(
            @PathVariable Long id,
            @RequestBody ShoppingListItemRequestDTO request,
            Principal principal
    ) {
        // ShoppingListItemResponseDTO updated = shoppingListService.updateItem(id, principal.getName(), request);
        return ResponseEntity.ok(Map.of("message", "Item updated successfully"));
//        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteItem(
            @PathVariable Long id,
            Principal principal
    ) {
        shoppingListService.deleteItem(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Item deleted successfully"));
    }
}
        package com.homeroha.controller;

        import com.homeroha.dto.ShoppingListItemRequestDTO;
        import com.homeroha.service.ShoppingListService;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.security.Principal;
        import java.util.Map;

        @RestController
        @RequestMapping("/api/shopping-list")
        @RequiredArgsConstructor
        public class ShoppingListController {

            private final ShoppingListService shoppingListService;

            @GetMapping
            public ResponseEntity<?> listItems(
                    @RequestParam("homeId") Long homeId,
                    Principal principal
            ) {
                return ResponseEntity.ok(shoppingListService.getItems(principal.getName(), homeId));
            }


            @PostMapping("/add")
            public ResponseEntity<?> addItem(@RequestBody ShoppingListItemRequestDTO request, Principal principal) {
                return ResponseEntity.ok(shoppingListService.addItem(principal.getName(), request));
            }

            @PutMapping("/mark-bought/{id}")
            public ResponseEntity<?> markBought(@PathVariable Long id, Principal principal) {
                return ResponseEntity.ok(shoppingListService.markBought(id, principal.getName()));
            }

            @DeleteMapping("/delete/{id}")
            public ResponseEntity<?> deleteItem(@PathVariable Long id, Principal principal) {
                System.out.println("ID: " + id);
                shoppingListService.deleteItem(id, principal.getName());
                return ResponseEntity.ok(Map.of("message", "Item deleted successfully"));
            }
        }


package com.homeroha.service.impl;

import com.homeroha.dto.ShoppingListItemRequestDTO;
import com.homeroha.dto.ShoppingListItemResponseDTO;
import com.homeroha.exception.HomerohaException;
import com.homeroha.model.*;
import com.homeroha.repository.*;
import com.homeroha.service.InventoryService;
import com.homeroha.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListItemRepository shoppingListItemRepository;
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final UserHomeRepository userHomeRepository;

    @Override
    public List<ShoppingListItemResponseDTO> getItems(String userEmail, Long homeId) {
        System.out.println("getItems called with userEmail: " + userEmail + " and homeId: " + homeId);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home;
        if (homeId != null) {
            home = homeRepository.findById(homeId)
                    .orElseThrow(() -> new HomerohaException("Home not found"));
        } else {
            home = user.getActiveHome();
            if (home == null) {
                throw new HomerohaException("No active home selected");
            }
        }

        boolean isMember = userHomeRepository.existsByUserAndHome(user, home);
        if (!isMember) {
            throw new HomerohaException("You are not a member of this home");
        }

        // ✅ Filter by home
        List<ShoppingListItem> items = shoppingListItemRepository.findAllByHome(home);

//                .findAllByHomeAndCreatedBy(home, user);

        return items.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ShoppingListItemResponseDTO> getItems(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home activeHome = user.getActiveHome();
        if (activeHome == null) {
            throw new HomerohaException("No active home selected");
        }

        List<ShoppingListItem> items = shoppingListItemRepository.findAllByHome(activeHome);

        return items.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ShoppingListItemResponseDTO addItem(String userEmail, ShoppingListItemRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home;
        if (request.getHomeId() != null) {
            home = homeRepository.findById(request.getHomeId())
                    .orElseThrow(() -> new HomerohaException("Home not found"));
        } else {
            home = user.getActiveHome();
            if (home == null) {
                throw new HomerohaException("No active home selected");
            }
        }

        boolean isMember = userHomeRepository.existsByUserAndHome(user, home);
        if (!isMember) {
            throw new HomerohaException("You are not a member of this home");
        }

        ShoppingListItem item = ShoppingListItem.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .category(request.getCategory())
                .bought(false)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .home(home)
                .build();

        shoppingListItemRepository.save(item);

        return mapToResponse(item);
    }

    @Override
    public ShoppingListItemResponseDTO markBought(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Item not found"));

        Home home = item.getHome(); // ✅ Direct from item

        boolean isMember = userHomeRepository.existsByUserAndHome(user, home);
        if (!isMember) {
            throw new HomerohaException("You are not a member of this home");
        }

        item.setBought(true);
        shoppingListItemRepository.save(item);

        InventoryItem inventoryItem = InventoryItem.builder()
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .threshold(1)
                .category(item.getCategory())
                .home(home) // ✅ Correct home
                .build();

        inventoryItemRepository.save(inventoryItem);

        return mapToResponse(item);
    }


    @Override
    public void deleteItem(Long id, String userEmail) {
        // 1) Load the user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        // 2) Load the item (and its home) in one go
        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Shopping item not found"));

        Home home = item.getHome();

        // 3) Check that the user belongs to that home
        if (!userHomeRepository.existsByUserAndHome(user, home)) {
            // Throw a 403
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not a member of this home"
            );
        }

        // 4) All good → delete
        shoppingListItemRepository.delete(item);
    }


    private ShoppingListItemResponseDTO mapToResponse(ShoppingListItem item) {
        return ShoppingListItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .category(item.getCategory())
                .bought(item.isBought())
                .createdBy(item.getCreatedBy().getName())
                .createdAt(item.getCreatedAt())
                .build();
    }
}

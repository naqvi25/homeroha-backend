package com.homeroha.service.impl;

import com.homeroha.dto.ShoppingListItemRequestDTO;
import com.homeroha.dto.ShoppingListItemResponseDTO;
import com.homeroha.exception.HomerohaException;
import com.homeroha.model.Home;
import com.homeroha.model.InventoryItem;
import com.homeroha.model.ShoppingListItem;
import com.homeroha.model.User;
import com.homeroha.repository.InventoryItemRepository;
import com.homeroha.repository.ShoppingListItemRepository;
import com.homeroha.repository.UserHomeRepository;
import com.homeroha.repository.UserRepository;
import com.homeroha.repository.HomeRepository;
import com.homeroha.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home = (homeId != null)
                ? homeRepository.findById(homeId).orElseThrow(() -> new HomerohaException("Home not found"))
                : getActiveHomeOrThrow(user);

        checkMembership(user, home);

        return shoppingListItemRepository.findAllByHome(home)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ShoppingListItemResponseDTO> getItems(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        Home home = getActiveHomeOrThrow(user);
        return shoppingListItemRepository.findAllByHome(home)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ShoppingListItemResponseDTO addItem(String userEmail, ShoppingListItemRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        Home home = (request.getHomeId() != null)
                ? homeRepository.findById(request.getHomeId()).orElseThrow(() -> new HomerohaException("Home not found"))
                : getActiveHomeOrThrow(user);

        checkMembership(user, home);

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
    @Transactional
    public ShoppingListItemResponseDTO markBought(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Item not found"));
        Home home = item.getHome();
        checkMembership(user, home);

        item.setBought(true);
        shoppingListItemRepository.save(item);

        InventoryItem inventoryItem = InventoryItem.builder()
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .threshold(1)
                .category(item.getCategory())
                .shoppingListItemId(item.getId())
                .home(home)
                .build();
        inventoryItemRepository.save(inventoryItem);

        return mapToResponse(item);
    }

    @Override
    @Transactional
    public ShoppingListItemResponseDTO unmarkBought(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Item not found"));
        Home home = item.getHome();
        checkMembership(user, home);

        item.setBought(false);
        shoppingListItemRepository.save(item);

        // Remove the linked inventory entry
        inventoryItemRepository.deleteByShoppingListItemId(item.getId());

        return mapToResponse(item);
    }

    @Override
    public void deleteItem(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Shopping item not found"));
        Home home = item.getHome();
        checkMembership(user, home);
        shoppingListItemRepository.delete(item);
    }

    @Override
    @Transactional
    public ShoppingListItemResponseDTO updateItem(Long id, String userEmail, ShoppingListItemRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Item not found"));
        Home home = item.getHome();
        checkMembership(user, home);

        // Update fields
        item.setName(request.getName());
        item.setQuantity(request.getQuantity());
        item.setUnit(request.getUnit());
        item.setCategory(request.getCategory());

        shoppingListItemRepository.save(item);
        return mapToResponse(item);
    }

    // helpers
    private void checkMembership(User user, Home home) {
        if (!userHomeRepository.existsByUserAndHome(user, home)) {
            throw new HomerohaException("You are not a member of this home");
        }
    }

    private Home getActiveHomeOrThrow(User user) {
        Home activeHome = user.getActiveHome();
        if (activeHome == null) {
            throw new HomerohaException("No active home selected");
        }
        return activeHome;
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
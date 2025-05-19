package com.homeroha.service.impl;

import com.homeroha.dto.InventoryItemRequestDTO;
import com.homeroha.dto.InventoryItemResponseDTO;
import com.homeroha.exception.HomerohaException;
import com.homeroha.model.*;
import com.homeroha.repository.*;
import com.homeroha.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;

    @Override
    public InventoryItemResponseDTO addItem(String userEmail, InventoryItemRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home homeToUse;

        if (request.getHomeId() != null) {
            // ✅ If homeId is provided in request, use it
            homeToUse = homeRepository.findById(request.getHomeId())
                    .orElseThrow(() -> new HomerohaException("Home not found"));
        } else {
            // ✅ Otherwise fallback to user's active home
            homeToUse = user.getActiveHome();
            if (homeToUse == null) {
                throw new HomerohaException("No active home selected");
            }
        }

        InventoryItem item = InventoryItem.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .threshold(request.getThreshold())
                .category(request.getCategory())
                .home(homeToUse) // ✅ Now using correct Home
                .build();

        inventoryItemRepository.save(item);

        return InventoryItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .threshold(item.getThreshold())
                .category(item.getCategory())
                .build();
    }

    @Override
    public List<InventoryItemResponseDTO> getAllItems(String userEmail, Long homeId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home = resolveHome(user, homeId); // ✅ Use helper function

        return inventoryItemRepository.findAllByHome(home)
                .stream()
                .map(item -> InventoryItemResponseDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .unit(item.getUnit())
                        .threshold(item.getThreshold())
                        .category(item.getCategory())
                        .build())
                .toList();
    }

    @Override
    public List<InventoryItemResponseDTO> getLowStockItems(String userEmail, Long homeId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home = resolveHome(user, homeId);

        return inventoryItemRepository.findAllByHomeAndQuantityLessThanEqual(home, 0)
                .stream()
                .map(item -> InventoryItemResponseDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .unit(item.getUnit())
                        .threshold(item.getThreshold())
                        .category(item.getCategory())
                        .build())
                .toList();
    }

    @Override
    public InventoryItemResponseDTO updateItem(Long id, String userEmail, InventoryItemRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home = user.getActiveHome();
        if (home == null) throw new HomerohaException("No active home selected");

        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Item not found"));

        if (!item.getHome().getId().equals(home.getId())) {
            throw new HomerohaException("Unauthorized to update this item");
        }

        item.setName(request.getName());
        item.setQuantity(request.getQuantity());
        item.setUnit(request.getUnit());
        item.setThreshold(request.getThreshold());
        item.setCategory(request.getCategory());

        inventoryItemRepository.save(item);

        return InventoryItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .threshold(item.getThreshold())
                .category(item.getCategory())
                .build();
    }

    @Override
    public void deleteItem(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        Home home = user.getActiveHome();
        if (home == null) throw new HomerohaException("No active home selected");

        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Item not found"));

        if (!item.getHome().getId().equals(home.getId())) {
            throw new HomerohaException("You are not authorized to delete this item");
        }

        inventoryItemRepository.delete(item);
    }

    private Home resolveHome(User user, Long homeId) {
        if (homeId != null) {
            return homeRepository.findById(homeId)
                    .orElseThrow(() -> new HomerohaException("Home not found"));
        } else {
            Home activeHome = user.getActiveHome();
            if (activeHome == null) {
                throw new HomerohaException("No active home selected");
            }
            return activeHome;
        }
    }


}
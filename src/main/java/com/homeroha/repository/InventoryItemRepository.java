package com.homeroha.repository;

import com.homeroha.model.Home;
import com.homeroha.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findAllByHome(Home home);
    List<InventoryItem> findAllByHomeAndQuantityLessThanEqual(Home home, Integer threshold);

    // Delete the inventory entry created by a shopping-list item
    void deleteByShoppingListItemId(Long shoppingListItemId);
}
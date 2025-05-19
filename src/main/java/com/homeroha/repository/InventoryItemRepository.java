package com.homeroha.repository;

import com.homeroha.model.Home;
import com.homeroha.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findAllByHome(Home home);
    List<InventoryItem> findAllByHomeAndQuantityLessThanEqual(Home home, Integer threshold);
}

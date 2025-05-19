package com.homeroha.repository;

import com.homeroha.model.Home;
import com.homeroha.model.ShoppingListItem;
import com.homeroha.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    List<ShoppingListItem> findAllByHome(Home home);
    List<ShoppingListItem> findAllByHomeAndCreatedBy(Home home, User createdBy); // ✅ New method
}

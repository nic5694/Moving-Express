package com.example.backend.inventorysubdomain.datalayer.Item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByInventoryIdentifier_InventoryId(String inventoryId);
    Item findByItemIdentifier_ItemId(String itemId);
}
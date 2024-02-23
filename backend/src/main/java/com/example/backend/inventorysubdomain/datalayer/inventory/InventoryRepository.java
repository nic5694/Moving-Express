package com.example.backend.inventorysubdomain.datalayer.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findByInventoryIdentifier_InventoryId(String inventoryId);
    List<Inventory> findAllByShipmentIdentifier_ShipmentId(String shipmentId);
}

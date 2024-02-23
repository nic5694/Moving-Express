package com.example.backend.inventorysubdomain.buisnesslayer.inventory;

import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    InventoryResponseModel getInventoryById(String inventoryId, Optional<String> observerCode );
    InventoryResponseModel addInventory(InventoryRequestModel inventoryRequestModel, String shipmentId, Optional<String> observerCode);
    InventoryResponseModel editInventory(InventoryRequestModel inventoryRequestModel, String inventoryId, Optional<String> observerCode);
    void deleteInventory(String inventoryId, Optional<String> observerCode);
    List<InventoryResponseModel> getInventories(String shipmentId, Optional<String> observerCode);
}

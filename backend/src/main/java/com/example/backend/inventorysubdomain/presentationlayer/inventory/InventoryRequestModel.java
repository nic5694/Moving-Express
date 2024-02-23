package com.example.backend.inventorysubdomain.presentationlayer.inventory;

import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Value
@Builder
public class InventoryRequestModel {
    String name;
    String description;
    private InventoryStatus inventoryStatus;
}

package com.example.backend.inventorysubdomain.presentationlayer.inventory;

import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class InventoryResponseModel {
    private String name;
    private String inventoryId;
    private String shipmentId;
    private String description;
    private InventoryStatus inventoryStatus;
    private Double approximateWeight;
}

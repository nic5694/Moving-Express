package com.example.backend.inventorysubdomain.presentationlayer.inventoryItems;

import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class InventoryItemsResponseModel {
    String name;
    String inventoryId;
    String shipmentId;
    String description;
    InventoryStatus inventoryStatus;
    Double approximateWeight;
    List<ItemResponseModel> items;
}

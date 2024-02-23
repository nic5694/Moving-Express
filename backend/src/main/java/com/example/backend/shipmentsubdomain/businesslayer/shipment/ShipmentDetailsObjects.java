package com.example.backend.shipmentsubdomain.businesslayer.shipment;

import com.example.backend.inventorysubdomain.presentationlayer.inventoryItems.InventoryItemsResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.ShipmentResponseModel;

import java.util.List;

public class ShipmentDetailsObjects {
    ShipmentResponseModel shipmentResponseModel;
    List<InventoryItemsResponseModel> inventoryItemsResponseModels;

    public ShipmentDetailsObjects(ShipmentResponseModel shipmentResponseModel, List<InventoryItemsResponseModel> inventoryItemsResponseModels) {
        this.shipmentResponseModel = shipmentResponseModel;
        this.inventoryItemsResponseModels = inventoryItemsResponseModels;
    }
}

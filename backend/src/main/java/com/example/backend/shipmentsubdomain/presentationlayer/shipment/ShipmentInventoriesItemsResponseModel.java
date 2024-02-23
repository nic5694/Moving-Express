package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import com.example.backend.inventorysubdomain.presentationlayer.inventoryItems.InventoryItemsResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Builder
@Value
@AllArgsConstructor
public class ShipmentInventoriesItemsResponseModel {
    String shipmentId;
    AddressResponseModel pickupAddress;
    AddressResponseModel destinationAddress;
    String userId;
    String truckId;
    Status status;
    String shipmentName;
    double approximateWeight;
    double weight;
    String email;
    String phoneNumber;
    LocalDate expectedMovingDate;
    LocalDate actualMovingDate;
    String firstName;
    String lastName;
    double approximateShipmentValue;
    List<InventoryItemsResponseModel> inventoriesItemsResponseModels;
}

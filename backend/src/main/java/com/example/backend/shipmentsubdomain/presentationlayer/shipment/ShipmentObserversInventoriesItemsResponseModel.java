package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventoryItems.InventoryItemsResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class ShipmentObserversInventoriesItemsResponseModel {
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
    List<ObserverResponseModel> observers;
    List<InventoryItemsResponseModel> inventoriesItemsResponseModels;
}

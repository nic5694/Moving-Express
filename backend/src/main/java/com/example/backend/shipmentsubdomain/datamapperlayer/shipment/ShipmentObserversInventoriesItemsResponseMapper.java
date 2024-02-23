package com.example.backend.shipmentsubdomain.datamapperlayer.shipment;

import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventoryItems.InventoryItemsResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.ShipmentObserversInventoriesItemsResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.ShipmentResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring" , uses = ShipmentObserversInventoriesItemsResponseMapper.class)
public interface ShipmentObserversInventoriesItemsResponseMapper {


    @Mapping(source = "shipmentResponseModel.shipmentId", target = "shipmentId")
    @Mapping(source = "shipmentResponseModel.pickupAddress", target = "pickupAddress")
    @Mapping(source = "shipmentResponseModel.destinationAddress", target = "destinationAddress")
    @Mapping(source = "shipmentResponseModel.userId", target = "userId")
    @Mapping(source = "shipmentResponseModel.truckId", target = "truckId")
    @Mapping(source = "shipmentResponseModel.status", target = "status")
    @Mapping(source = "shipmentResponseModel.shipmentName", target = "shipmentName")
    @Mapping(source = "shipmentResponseModel.approximateWeight", target = "approximateWeight")
    @Mapping(source = "shipmentResponseModel.weight", target = "weight")
    @Mapping(source = "shipmentResponseModel.email", target = "email")
    @Mapping(source = "shipmentResponseModel.phoneNumber", target = "phoneNumber")
    @Mapping(source = "shipmentResponseModel.expectedMovingDate", target = "expectedMovingDate")
    @Mapping(source = "shipmentResponseModel.actualMovingDate", target = "actualMovingDate")
    @Mapping(source = "shipmentResponseModel.firstName", target = "firstName")
    @Mapping(source = "shipmentResponseModel.lastName", target = "lastName")
    @Mapping(source = "shipmentResponseModel.approximateShipmentValue", target = "approximateShipmentValue")
    @Mapping(source = "inventoryItemsResponseModels", target = "inventoriesItemsResponseModels")
    @Mapping(source = "observerResponseModels", target = "observers")
    ShipmentObserversInventoriesItemsResponseModel mapShipmentInventoryItemsObserversToResponseModel(ShipmentResponseModel shipmentResponseModel,
                                                                                                     List<InventoryItemsResponseModel> inventoryItemsResponseModels,
                                                                                                     List<ObserverResponseModel> observerResponseModels);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "inventoryId", target = "inventoryId")
    @Mapping(source = "shipmentId", target = "shipmentId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "inventoryStatus", target = "inventoryStatus")
    @Mapping(source = "approximateWeight", target = "approximateWeight")
    @Mapping(source = "items", target = "items")
    InventoryItemsResponseModel mapInventoryItemsResponseModel(InventoryItemsResponseModel inventoryItemsResponseModel);
}

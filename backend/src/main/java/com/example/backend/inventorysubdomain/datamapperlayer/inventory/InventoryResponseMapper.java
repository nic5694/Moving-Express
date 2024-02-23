package com.example.backend.inventorysubdomain.datamapperlayer.inventory;

import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryResponseMapper {

    @Mappings({
            @Mapping(expression = "java(inventory.getShipmentIdentifier().getShipmentId())", target = "shipmentId"),
            @Mapping(expression = "java(inventory.getInventoryIdentifier().getInventoryId())", target = "inventoryId"),
            @Mapping(expression = "java(inventory.getInventoryStatus())", target = "inventoryStatus"),
            @Mapping(expression = "java(inventory.getApproximateWeight())", target = "approximateWeight"),
            @Mapping(expression = "java(inventory.getName())", target = "name"),
            @Mapping(expression = "java(inventory.getDescription())", target = "description")
    })
    InventoryResponseModel entityToResponseModel(Inventory inventory);
    List<InventoryResponseModel> entitiesToResponseModels(List<Inventory> inventories);
}

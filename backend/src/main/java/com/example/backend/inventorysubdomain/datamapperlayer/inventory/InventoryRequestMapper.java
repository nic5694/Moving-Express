package com.example.backend.inventorysubdomain.datamapperlayer.inventory;

import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InventoryRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "inventoryIdentifier", ignore = true),
            @Mapping(target = "shipmentIdentifier", ignore = true),
            @Mapping(target = "inventoryStatus", ignore = true)
    })
    Inventory requestModelToEntity(InventoryRequestModel inventoryRequestModel);
}

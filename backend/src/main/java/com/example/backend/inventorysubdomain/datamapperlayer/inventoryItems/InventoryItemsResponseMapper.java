package com.example.backend.inventorysubdomain.datamapperlayer.inventoryItems;

import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventoryItems.InventoryItemsResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InventoryItemsResponseMapper.class})
public interface InventoryItemsResponseMapper {
    @Mapping(source = "inventoryResponseModel.name", target = "name")
    @Mapping(source = "inventoryResponseModel.inventoryId", target = "inventoryId")
    @Mapping(source = "inventoryResponseModel.shipmentId", target = "shipmentId")
    @Mapping(source = "inventoryResponseModel.description", target = "description")
    @Mapping(source = "inventoryResponseModel.inventoryStatus", target = "inventoryStatus")
    @Mapping(source = "inventoryResponseModel.approximateWeight", target = "approximateWeight")
    @Mapping(source = "itemResponseModels", target = "items")
    InventoryItemsResponseModel mapInventoryAndItemsListToResponseModel(InventoryResponseModel inventoryResponseModel, List<ItemResponseModel> itemResponseModels);
}

package com.example.backend.inventorysubdomain.datamapperlayer.Item;

import com.example.backend.inventorysubdomain.datalayer.Item.Item;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemResponseMapper {
    @Mappings({
            @Mapping(expression = "java(item.getItemIdentifier().getItemId())", target = "itemId"),
            @Mapping(expression = "java(item.getName())", target = "name"),
            @Mapping(expression = "java(item.getType())", target = "type"),
            @Mapping(expression = "java(item.getPrice())", target = "price"),
            @Mapping(expression = "java(item.getDescription())", target = "description"),
            @Mapping(expression = "java(item.getWeight())", target = "weight"),
            @Mapping(expression = "java(item.getHandlingInstructions())", target = "handlingInstructions"),
            @Mapping(expression = "java(item.getInventoryIdentifier().getInventoryId())", target = "inventoryId")
    })
    ItemResponseModel entityToResponseModel(Item item);
    List<ItemResponseModel> entitiesToResponseModels(List<Item> items);
}
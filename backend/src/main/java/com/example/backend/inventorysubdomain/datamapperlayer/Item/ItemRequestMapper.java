package com.example.backend.inventorysubdomain.datamapperlayer.Item;

import com.example.backend.inventorysubdomain.datalayer.Item.Item;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "itemIdentifier", ignore = true),
            @Mapping(target = "inventoryIdentifier", ignore = true),
    })
    Item requestModelToEntity(ItemRequestModel itemRequestModel);
}
package com.example.backend.shipmentsubdomain.datamapperlayer.observer;

import com.example.backend.shipmentsubdomain.datalayer.observer.Observer;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface ObserverRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "observerIdentifier", ignore = true),
            @Mapping(target = "shipmentIdentifier", ignore = true)
    })
    Observer requestModelToEntity(ObserverRequestModel observerRequestModel);
}



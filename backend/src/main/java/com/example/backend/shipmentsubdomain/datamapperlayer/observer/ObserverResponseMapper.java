package com.example.backend.shipmentsubdomain.datamapperlayer.observer;

import com.example.backend.shipmentsubdomain.datalayer.observer.Observer;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ObserverResponseMapper {
    @Mappings({
            @Mapping(expression = "java(observer.getObserverIdentifier().getObserverId())", target = "observerId"),
            @Mapping(expression = "java(observer.getShipmentIdentifier().getShipmentId())", target = "shipmentId"),
            @Mapping(expression = "java(observer.getName())", target = "name"),
            @Mapping(expression = "java(observer.getObserverCode())", target = "observerCode"),
            @Mapping(expression = "java(observer.getPermission())", target = "permission")
    })
    ObserverResponseModel entityToResponseModel(Observer observer);

    List<ObserverResponseModel> entitiesToResponseModels(List<Observer> observers);

}

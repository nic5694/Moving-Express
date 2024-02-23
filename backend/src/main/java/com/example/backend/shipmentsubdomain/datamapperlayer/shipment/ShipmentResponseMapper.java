package com.example.backend.shipmentsubdomain.datamapperlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.ShipmentResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel="spring" , uses = AddressResponseMapper.class)
public interface ShipmentResponseMapper {
    @Mappings({
            @Mapping(expression = "java(shipment.getShipmentIdentifier().getShipmentId())", target = "shipmentId"),
            @Mapping(target = "pickupAddress", source = "pickupAddress"),
            @Mapping(target = "destinationAddress", source = "destinationAddress"),
            @Mapping(expression = "java(shipment.getUserId())", target = "userId"),
            @Mapping(source = "truckIdentifier.vin", target = "truckId"),
            @Mapping(expression = "java(shipment.getDriverId())", target = "driverId"),
            @Mapping(expression = "java(shipment.getName())", target = "shipmentName"),
            @Mapping(expression = "java(shipment.getApproximateWeight())", target = "approximateWeight"),
            @Mapping(expression = "java(shipment.getWeight())", target = "weight"),
            @Mapping(expression = "java(shipment.getStatus())", target = "status"),
            @Mapping(expression = "java(shipment.getEmail())", target = "email"),
            @Mapping(expression = "java(shipment.getFirstName())", target = "firstName"),
            @Mapping(expression = "java(shipment.getLastName())", target = "lastName"),
            @Mapping(expression = "java(shipment.getApproximateShipmentValue())", target = "approximateShipmentValue")
    })
    ShipmentResponseModel entityToResponseModel(Shipment shipment);
    List<ShipmentResponseModel> entitiesToResponseModel(List<Shipment> shipments);
}

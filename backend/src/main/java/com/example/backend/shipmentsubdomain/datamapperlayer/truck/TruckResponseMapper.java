package com.example.backend.shipmentsubdomain.datamapperlayer.truck;

import com.example.backend.shipmentsubdomain.datalayer.truck.Truck;
import com.example.backend.shipmentsubdomain.presentationlayer.truck.TruckResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TruckResponseMapper {
    @Mapping(target = "vin", source = "truckId.vin")
    @Mapping(target = "capacity", source = "capacity")
    TruckResponseModel toTruckResponseModel(Truck truck);

    List<TruckResponseModel> toTruckResponseModelList(List<Truck> trucks);
}

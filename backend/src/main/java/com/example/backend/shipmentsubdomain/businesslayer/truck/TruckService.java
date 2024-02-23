package com.example.backend.shipmentsubdomain.businesslayer.truck;

import com.example.backend.shipmentsubdomain.presentationlayer.truck.TruckResponseModel;

import java.util.List;

public interface TruckService {
    List<TruckResponseModel> getAllTrucks();
}

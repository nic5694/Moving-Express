package com.example.backend.shipmentsubdomain.presentationlayer.truck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class TruckResponseModel {
    private String vin;
    private double capacity;
}

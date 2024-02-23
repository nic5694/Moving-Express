package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class FinalWeightRequestModel {
    private String shipmentId;
    private double finalWeight;
}

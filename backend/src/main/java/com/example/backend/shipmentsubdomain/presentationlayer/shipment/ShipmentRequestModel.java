package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class ShipmentRequestModel {
    private String pickupAddressId;
    private String destinationAddressId;
    private String clientId;
    private String truckId;
    private Status status;
    private double approximateWeight;
    private double approximateShipmentValue;
}

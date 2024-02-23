package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.truck.TruckIdentifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class AssignDriverAndTruckToShipmentRequest {
    private String shipmentId;
    private TruckIdentifier vin;
    private String driverId;

}

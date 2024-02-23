package com.example.backend.shipmentsubdomain.datalayer.shipment;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
public class ShipmentIdentifier {

    @Getter
    private String shipmentId;

    public ShipmentIdentifier(){
        this.shipmentId= UUID.randomUUID().toString();
    }
    public ShipmentIdentifier(String shipmentId){
        this.shipmentId = shipmentId;
    }
}

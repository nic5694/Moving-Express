package com.example.backend.shipmentsubdomain.datalayer.observer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
public class ObserverIdentifier {

    @Getter
    private String observerId;

    public ObserverIdentifier(){
        this.observerId = java.util.UUID.randomUUID().toString();
    }
    public ObserverIdentifier(String observerId){
        this.observerId = observerId;
    }
}

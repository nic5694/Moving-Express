package com.example.backend.shipmentsubdomain.datalayer.observer;


import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity(name = "observers")
@Builder
@AllArgsConstructor
public class Observer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ObserverIdentifier observerIdentifier;

    @Embedded
    private ShipmentIdentifier shipmentIdentifier;

    private String name;
    private String observerCode;
    private Permission permission;

    public Observer() {
        this.observerIdentifier = new ObserverIdentifier();
    }

    public Observer(String shipmentId, String name, String observerCode, Permission permission) {
        this.observerIdentifier = new ObserverIdentifier();
        this.shipmentIdentifier = new ShipmentIdentifier(shipmentId);
        this.name = name;
        this.observerCode = observerCode;
        this.permission = permission;
    }

}

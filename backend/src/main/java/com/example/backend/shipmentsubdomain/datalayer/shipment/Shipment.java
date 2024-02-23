package com.example.backend.shipmentsubdomain.datalayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.truck.TruckIdentifier;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity(name = "shipments")
@Builder
@AllArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private ShipmentIdentifier shipmentIdentifier;
    @Nullable
    private String userId;
    @Nullable
    private String driverId;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    @Nullable
    @Embedded
    private TruckIdentifier truckIdentifier;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate expectedMovingDate;
    @Nullable
    private LocalDate actualMovingDate;
    @Nullable
    private double approximateWeight;
    @Nullable
    private double weight;
    private String name;
    @Nullable
    private double approximateShipmentValue;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "pickup_address_id", referencedColumnName = "addressId")
    private Address pickupAddress;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "destination_address_id", referencedColumnName = "addressId")
    private Address destinationAddress;

    public Shipment(String userId, TruckIdentifier truckIdentifier,
                    Status status, LocalDate expectedMovingDate,
                    LocalDate actualMovingDate, double approximateWeight,
                    String name, Address pickupAddress, Address destinationAddress,
                    String email, String phoneNumber, double approximateShipmentValue, String driverId) {
        this.userId = userId;
        this.truckIdentifier = truckIdentifier;
        this.status = status;
        this.expectedMovingDate = expectedMovingDate;
        this.actualMovingDate = actualMovingDate;
        this.approximateWeight = approximateWeight;
        this.name = name;
        this.pickupAddress = pickupAddress;
        this.destinationAddress = destinationAddress;
        this.shipmentIdentifier = new ShipmentIdentifier();
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.approximateShipmentValue = approximateShipmentValue;
        this.driverId = driverId;
    }

    public Shipment() {
        this.shipmentIdentifier = new ShipmentIdentifier();
    }
}
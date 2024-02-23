package com.example.backend.shipmentsubdomain.datalayer.truck;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity(name = "trucks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Truck {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @Embedded
    private TruckIdentifier truckId;
    @Nullable
    private double capacity;
}

package com.example.backend.inventorysubdomain.datalayer.inventory;

import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Entity(name = "inventories")
@Builder
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private InventoryIdentifier inventoryIdentifier;
    @Embedded
    private ShipmentIdentifier shipmentIdentifier;
    private String name;
    @Nullable
    private String description;
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

    private Double approximateWeight;

    public Inventory(ShipmentIdentifier shipmentIdentifier, String name, @Nullable String description, Double approximateWeight) {
        this.inventoryIdentifier = new InventoryIdentifier();
        this.shipmentIdentifier = shipmentIdentifier;
        this.name = name;
        this.description = description;
        this.inventoryStatus = InventoryStatus.CREATED;
        this.approximateWeight=approximateWeight;
    }

    public Inventory(){
        this.inventoryIdentifier = new InventoryIdentifier();
    }
}

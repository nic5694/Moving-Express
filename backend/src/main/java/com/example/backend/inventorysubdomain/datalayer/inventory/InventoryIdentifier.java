package com.example.backend.inventorysubdomain.datalayer.inventory;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable

public class InventoryIdentifier {
    @Getter
    private String inventoryId;
    public InventoryIdentifier(){
        this.inventoryId = UUID.randomUUID().toString();
    }
    public InventoryIdentifier(String inventoryId){
        this.inventoryId = inventoryId;
    }
}

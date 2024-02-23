package com.example.backend.inventorysubdomain.datalayer.Item;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable

public class ItemIdentifier {
    @Getter
    private String itemId;
    public ItemIdentifier(){
        this.itemId = UUID.randomUUID().toString();
    }
}
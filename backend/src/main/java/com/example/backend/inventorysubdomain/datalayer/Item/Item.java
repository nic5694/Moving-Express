package com.example.backend.inventorysubdomain.datalayer.Item;

import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryIdentifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name="items")
@Data
@Builder
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private ItemIdentifier itemIdentifier;
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;
    private BigDecimal price;
    private String description;
    private Double weight;
    private String handlingInstructions;
    private InventoryIdentifier inventoryIdentifier;

    public Item(){
        this.itemIdentifier=new ItemIdentifier();
    }

    public Item(String name, Type type, BigDecimal price, String description, Double weight, String handlingInstructions, InventoryIdentifier inventoryIdentifier) {
        this.itemIdentifier=new ItemIdentifier();
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;
        this.weight = weight;
        this.handlingInstructions = handlingInstructions;
        this.inventoryIdentifier = inventoryIdentifier;
    }
}
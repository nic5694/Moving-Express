package com.example.backend.inventorysubdomain.presentationlayer.Item;

import com.example.backend.inventorysubdomain.datalayer.Item.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

@Value
@AllArgsConstructor
@Builder
public class ItemRequestModel {
    private String name;
    private Type type;
    private BigDecimal price;
    private String description;
    private Double weight;
    private String handlingInstructions;
}
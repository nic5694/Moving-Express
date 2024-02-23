package com.example.backend.shipmentsubdomain.datalayer.quote;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class QuoteIdentifier {
    private String quoteId;

    public QuoteIdentifier(){
        this.quoteId= UUID.randomUUID().toString();
    }
}

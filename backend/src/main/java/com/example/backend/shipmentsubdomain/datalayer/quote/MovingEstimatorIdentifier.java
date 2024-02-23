package com.example.backend.shipmentsubdomain.datalayer.quote;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class MovingEstimatorIdentifier {
    private String movingEstimatorId;
    public MovingEstimatorIdentifier() {
        this.movingEstimatorId = UUID.randomUUID().toString();
    }

}

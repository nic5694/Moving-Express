package com.example.backend.shipmentsubdomain.datalayer.quote;

import com.example.backend.shipmentsubdomain.datalayer.Country;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DestinationAddress {
    private String destinationStreetAddress;
    private String destinationCity;
    @Enumerated(EnumType.STRING)
    private Country destinationCountry;
    private String destinationPostalCode;
    private int destinationNumberOfRooms;
    private boolean destinationElevator;
    private String destinationBuildingType;
}

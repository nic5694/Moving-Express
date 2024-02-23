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
public class PickupAddress {
    private String pickupStreetAddress;
    private String pickupCity;
    @Enumerated(EnumType.STRING)
    private Country pickupCountry;
    private String pickupPostalCode;
    private int pickupNumberOfRooms;
    private boolean pickupElevator;
    private String pickupBuildingType;
}

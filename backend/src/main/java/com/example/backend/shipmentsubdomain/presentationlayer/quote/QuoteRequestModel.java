package com.example.backend.shipmentsubdomain.presentationlayer.quote;

import com.example.backend.shipmentsubdomain.datalayer.quote.ContactMethod;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class QuoteRequestModel {
    private String pickupStreetAddress;
    private String pickupCity;
    private Country pickupCountry;
    private String pickupPostalCode;
    private int pickupNumberOfRooms;
    private boolean pickupElevator;
    private String pickupBuildingType;
    private String destinationStreetAddress;
    private String destinationCity;
    private Country destinationCountry;
    private String destinationPostalCode;
    private int destinationNumberOfRooms;
    private boolean destinationElevator;
    private String destinationBuildingType;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private LocalDate expectedMovingDate;
    private ContactMethod contactMethod;
    private String comment;
    private String shipmentName;
    private double approximateWeight;
    private double approximateShipmentValue;
}

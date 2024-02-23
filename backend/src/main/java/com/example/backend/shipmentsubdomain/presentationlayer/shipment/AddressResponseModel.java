package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class AddressResponseModel {
    private String addressId;
    private String streetAddress;
    private String city;
    private String postalCode;
    private Country country;

}

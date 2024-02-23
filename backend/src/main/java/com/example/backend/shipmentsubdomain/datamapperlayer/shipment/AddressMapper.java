package com.example.backend.shipmentsubdomain.datamapperlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressIdentifier", ignore = true)
    Address toAddress(String streetAddress, String city, String postalCode, Country country);
}
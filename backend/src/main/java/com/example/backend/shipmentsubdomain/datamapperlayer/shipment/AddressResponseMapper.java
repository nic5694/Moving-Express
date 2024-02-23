package com.example.backend.shipmentsubdomain.datamapperlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.AddressResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface AddressResponseMapper {
    @Mappings({
            @Mapping(expression = "java(address.getAddressIdentifier().getAddressId())", target = "addressId"),
            @Mapping(expression = "java(address.getStreetAddress())", target = "streetAddress"),
            @Mapping(expression = "java(address.getCity())", target = "city"),
            @Mapping(expression = "java(address.getPostalCode())", target = "postalCode"),
            @Mapping(expression = "java(address.getCountry())", target = "country")
    })
    AddressResponseModel entityToResponseModel(Address address);
}

package com.example.backend.shipmentsubdomain.datamapperlayer.quote;

import com.example.backend.shipmentsubdomain.datalayer.quote.Quote;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuoteResponseMapper {
    @Mappings({
            @Mapping(expression = "java(quote.getQuoteIdentifier().getQuoteId())", target = "quoteId"),
            @Mapping(expression = "java(quote.getPickupAddress().getPickupStreetAddress())", target = "pickupStreetAddress"),
            @Mapping(expression = "java(quote.getPickupAddress().getPickupCity())", target = "pickupCity"),
            @Mapping(expression = "java(quote.getPickupAddress().getPickupCountry())", target = "pickupCountry"),
            @Mapping(expression = "java(quote.getPickupAddress().getPickupPostalCode())", target = "pickupPostalCode"),
            @Mapping(expression = "java(quote.getPickupAddress().getPickupNumberOfRooms())", target = "pickupNumberOfRooms"),
            @Mapping(expression = "java(quote.getPickupAddress().isPickupElevator())", target = "pickupElevator"),
            @Mapping(expression = "java(quote.getPickupAddress().getPickupBuildingType())", target = "pickupBuildingType"),
            @Mapping(expression = "java(quote.getDestinationAddress().getDestinationStreetAddress())", target = "destinationStreetAddress"),
            @Mapping(expression = "java(quote.getDestinationAddress().getDestinationCity())", target = "destinationCity"),
            @Mapping(expression = "java(quote.getDestinationAddress().getDestinationCountry())", target = "destinationCountry"),
            @Mapping(expression = "java(quote.getDestinationAddress().getDestinationPostalCode())", target = "destinationPostalCode"),
            @Mapping(expression = "java(quote.getDestinationAddress().getDestinationNumberOfRooms())", target = "destinationNumberOfRooms"),
            @Mapping(expression = "java(quote.getDestinationAddress().isDestinationElevator())", target = "destinationElevator"),
            @Mapping(expression = "java(quote.getDestinationAddress().getDestinationBuildingType())", target = "destinationBuildingType"),
            @Mapping(expression = "java(quote.getContactDetails().getFirstName())", target = "firstName"),
            @Mapping(expression = "java(quote.getContactDetails().getLastName())", target = "lastName"),
            @Mapping(expression = "java(quote.getContactDetails().getPhoneNumber())", target = "phoneNumber"),
            @Mapping(expression = "java(quote.getContactDetails().getEmailAddress())", target = "emailAddress"),
            @Mapping(expression = "java(quote.getContactMethod())", target = "contactMethod"),
            @Mapping(expression = "java(quote.getInitiationDate())", target = "initiationDate"),
            @Mapping(expression = "java(quote.getExpectedMovingDate())", target = "expectedMovingDate"),
            @Mapping(expression = "java(quote.getComment())", target = "comment"),
            @Mapping(expression = "java(quote.getQuoteStatus())", target="quoteStatus"),
            @Mapping(expression = "java(quote.getShipmentName())", target="name"),
            @Mapping(expression = "java(quote.getApproximateWeight())", target="approximateWeight"),
            @Mapping(expression = "java(quote.getApproximateShipmentValue())", target="approximateShipmentValue")
    })
    QuoteResponseModel entityToResponseModel(Quote quote);
    List<QuoteResponseModel> entitiesListToResponseList(List<Quote> quotes);
}

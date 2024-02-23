package com.example.backend.shipmentsubdomain.datamapperlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteRequestModel;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ShipmentRequestMapper {

    Shipment quoteRequestToShipmentMapper(QuoteRequestModel quoteRequestModel);
}

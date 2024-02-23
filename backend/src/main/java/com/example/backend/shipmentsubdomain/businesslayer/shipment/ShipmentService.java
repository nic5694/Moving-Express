package com.example.backend.shipmentsubdomain.businesslayer.shipment;

import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.*;

import java.util.List;
import java.util.Optional;

public interface ShipmentService {
    ShipmentResponseModel createShipment(QuoteResponseModel quoteResponseModel);
    List<ShipmentResponseModel> getAllShipments(Optional<String> userId, Optional<String> email);
    ShipmentResponseModel getShipment(String shipmentId);
    EventResponseModel cancelShipment(String shipmentId);
    ShipmentResponseModel getShipmentDetailsByObserverCode(String observerCode);
    List<ShipmentResponseModel> getShipmentsByDriverId(String driverId);
    ShipmentObserversInventoriesItemsResponseModel getShipmentDetails(String shipmentId);
    ShipmentInventoriesItemsResponseModel getShipmentDetailsForDriverReport(String shipmentId);
    List<ShipmentResponseModel> getUnassignedShipments();
    ShipmentResponseModel assignTruckAndDriverToShipment(AssignDriverAndTruckToShipmentRequest assignDriverAndTruckToShipmentRequest);
    ShipmentResponseModel setShipmentFinalWeight(FinalWeightRequestModel finalWeightRequestModel);
    ShipmentResponseModel updateShipmentStatus(String shipmentId, ShipmentRequestModel shipmentRequestModel);
    ShipmentResponseModel unassignShipment(String shipmentId, String driverId);
}

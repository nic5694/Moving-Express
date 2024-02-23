package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import com.example.backend.shipmentsubdomain.businesslayer.shipment.ShipmentService;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.util.EmailUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping(path = "api/v1/movingexpress/shipments")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://movingexpress.systems"}, allowCredentials = "true")
public class ShipmentController {
    private final ShipmentService shipmentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<List<ShipmentResponseModel>> getAllShipments(@RequestParam Map<String, String> requestParams) {
        Optional<String> userId = Optional.ofNullable(requestParams.get("userId"));
        Optional<String> email = Optional.ofNullable(requestParams.get("email"));

        List<ShipmentResponseModel> shipments = shipmentService.getAllShipments(userId, email);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/{shipmentId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<ShipmentResponseModel> getShipment(@PathVariable String shipmentId){
        return ResponseEntity.ok(shipmentService.getShipment(shipmentId));
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('Shipment_Estimator')")
    public ResponseEntity<ShipmentResponseModel> createShipment(@RequestBody QuoteResponseModel quoteResponseModel){
        return ResponseEntity.status(CREATED).body(shipmentService.createShipment(quoteResponseModel));
    }

    @DeleteMapping("/{shipmentId}")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<EventResponseModel> cancelShipment(@PathVariable String shipmentId){
        return ResponseEntity.status(NO_CONTENT).body(shipmentService.cancelShipment(shipmentId));
    }

    @GetMapping("/observer/{observerCode}")
    public ResponseEntity<ShipmentResponseModel> getShipmentDetailsByObserverCode(@PathVariable String observerCode) {
        return ResponseEntity.ok(shipmentService.getShipmentDetailsByObserverCode(observerCode));
    }

    @GetMapping("/assigned/driver")
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public ResponseEntity<List<ShipmentResponseModel>> getShipmentsByDriverId(@AuthenticationPrincipal OidcUser driverId) {
        return ResponseEntity.ok(shipmentService.getShipmentsByDriverId(driverId.getSubject()));
    }

    @GetMapping("/{shipmentId}/details")
    @PreAuthorize("hasAnyAuthority('Shipment_Owner')")
    public ResponseEntity<ShipmentObserversInventoriesItemsResponseModel> getShipmentDetails(@PathVariable String shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipmentDetails(shipmentId));
    }

    @GetMapping("/{shipmentId}/driver-report")
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public ResponseEntity<ShipmentInventoriesItemsResponseModel> getShipmentDetailsForDriverReport(@PathVariable String shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipmentDetailsForDriverReport(shipmentId));
    }

    @GetMapping("/unassigned/driver")
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public ResponseEntity<List<ShipmentResponseModel>> getUnassignedShipments(){
        return ResponseEntity.ok(shipmentService.getUnassignedShipments());
    }

    @PutMapping()
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public ResponseEntity<ShipmentResponseModel> assignTruckAndDriverToShipment(@RequestBody AssignDriverAndTruckToShipmentRequest assignDriverAndTruckToShipmentRequest){
        return ResponseEntity.ok(shipmentService.assignTruckAndDriverToShipment(assignDriverAndTruckToShipmentRequest));
    }

    @PutMapping("/setShipmentFinalWeight")
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public ResponseEntity<ShipmentResponseModel> setShipmentFinalWeight(@RequestBody FinalWeightRequestModel finalWeightRequestModel){
        return ResponseEntity.ok(shipmentService.setShipmentFinalWeight(finalWeightRequestModel));
    }


    @PutMapping("/{shipmentId}")
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public ResponseEntity<ShipmentResponseModel> updateShipmentStatus(@PathVariable String shipmentId,
                                                                      @RequestBody ShipmentRequestModel shipmentRequestModel){
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(shipmentId, shipmentRequestModel));
    }
    
    @PutMapping("/{shipmentId}/driver")
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public ResponseEntity<ShipmentResponseModel> unassignShipment(@PathVariable String shipmentId,
                                                                  @AuthenticationPrincipal OidcUser driverId){
        return ResponseEntity.ok(shipmentService.unassignShipment(shipmentId, driverId.getSubject()));
    }
}

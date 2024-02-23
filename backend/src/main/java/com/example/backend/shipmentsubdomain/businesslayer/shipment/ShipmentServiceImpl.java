package com.example.backend.shipmentsubdomain.businesslayer.shipment;

import com.example.backend.inventorysubdomain.buisnesslayer.Item.ItemService;
import com.example.backend.inventorysubdomain.buisnesslayer.inventory.InventoryService;
import com.example.backend.inventorysubdomain.datamapperlayer.inventoryItems.InventoryItemsResponseMapper;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventoryItems.InventoryItemsResponseModel;
import com.example.backend.shipmentsubdomain.businesslayer.observer.ObserverService;
import com.example.backend.shipmentsubdomain.datamapperlayer.shipment.*;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.*;
import com.example.backend.usersubdomain.buisnesslayer.UserService;
import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.Address.AddressIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.Address.AddressRepository;
import com.example.backend.shipmentsubdomain.datalayer.observer.Observer;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverRepository;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.presentationlayer.UserResponseModel;
import com.example.backend.util.EmailUtil;
import com.example.backend.util.exceptions.*;
import com.mysql.cj.exceptions.UnableToConnectException;
import jakarta.transaction.Transactional;
import kotlin.Pair;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final TemplateEngine templateEngine;
    private final ShipmentRepository shipmentRepository;
    private final QuoteResponseToShipmentMapper quoteResponseToShipmentMapper;
    private final ShipmentResponseMapper shipmentResponseMapper;
    private final AddressMapper addressMapper;
    private final UserService userService;
    private final AddressRepository addressRepository;
    private final EmailUtil emailUtil;
    private final ObserverRepository observerRepository;
    private final InventoryService inventoryService;
    private final ItemService itemService;
    private final InventoryItemsResponseMapper inventoryItemsResponseMapper;
    private final ObserverService observerService;
    private final ShipmentObserversInventoriesItemsResponseMapper shipmentObserversInventoriesItemsResponseMapper;
    private final ShipmentInventoriesItemsResponseMapper shipmentInventoriesItemsResponseMapper;

    @Generated
    public String generateShipmentConfirmationEmailContentString(String shipmentId) {
        try {
            Context context = new Context();
            context.setVariable("shipmentID", shipmentId != null ? shipmentId : "");

            return templateEngine.process("shipmentConfirmation", context);
        } catch (Exception e) {
            throw new UnableToConnectException("Unable to send email");
        }
    }
    @Generated
    public String generateCancelShipmentEmailContentString(Shipment shipment) {
        try {
            Context context = new Context();
            context.setVariable("shipmentID", shipment.getShipmentIdentifier() != null ? shipment.getShipmentIdentifier().getShipmentId() : "");
            context.setVariable("pickupAddress", shipment.getPickupAddress() != null ? shipment.getPickupAddress() : "");
            context.setVariable("destinationAddress", shipment.getDestinationAddress() != null ? shipment.getDestinationAddress() : "");
            return templateEngine.process("shipmentCancellationConfirmation", context);
        } catch (Exception e) {
            throw new UnableToConnectException("Unable to send email");
        }
    }


    @Override
    @Transactional
    public ShipmentResponseModel createShipment(QuoteResponseModel quoteResponseModel) {
        if(quoteResponseModel.getApproximateWeight()<=0.0){
            throw new InvalidWeightException("invalid approximateWeight: "+quoteResponseModel.getApproximateWeight());
        }

        if(quoteResponseModel.getApproximateShipmentValue()<=0.0){
            throw new InvalidValueException("invalid shipmentValue: "+quoteResponseModel.getApproximateShipmentValue());
        }

        // Create and save the departure address
        Address pickupAddress = addressMapper.toAddress(
                quoteResponseModel.getPickupStreetAddress(),
                quoteResponseModel.getPickupCity(),
                quoteResponseModel.getPickupPostalCode(),
                quoteResponseModel.getPickupCountry());
        pickupAddress.setAddressIdentifier(new AddressIdentifier());
        Address savedDepartureAddress = addressRepository.save(pickupAddress);

        // Create and save the arrival address
        Address destinationAddress = addressMapper.toAddress(
                quoteResponseModel.getDestinationStreetAddress(),
                quoteResponseModel.getDestinationCity(),
                quoteResponseModel.getDestinationPostalCode(),
                quoteResponseModel.getDestinationCountry());
        destinationAddress.setAddressIdentifier(new AddressIdentifier());
        Address savedArrivalAddress = addressRepository.save(destinationAddress);

        // Map the QuoteResponseModel to Shipment using your mapper
        Shipment shipment = quoteResponseToShipmentMapper.toShipment(quoteResponseModel, addressMapper);

        UserResponseModel user = userService.getUserByEmail(quoteResponseModel.getEmailAddress());

        // Set the saved addresses in the Shipment entity
        shipment.setPickupAddress(savedDepartureAddress);
        shipment.setDestinationAddress(savedArrivalAddress);
        shipment.setStatus(Status.QUOTED);
        shipment.setEmail(quoteResponseModel.getEmailAddress());
        shipment.setShipmentIdentifier(new ShipmentIdentifier());
        shipment.setUserId(user.getUserId());
        shipment.setFirstName(user.getFirstName());
        shipment.setLastName(user.getLastName());
//        shipment.setPhoneNumber(user.getPhoneNumber());
        shipment.setPhoneNumber(quoteResponseModel.getPhoneNumber());
        shipment.setApproximateWeight(quoteResponseModel.getApproximateWeight());
        shipment.setApproximateShipmentValue(quoteResponseModel.getApproximateShipmentValue());

        // Save the shipment
        Shipment savedShipment = shipmentRepository.save(shipment);
        //send email to user
        emailUtil.SslEmail(shipment.getEmail(), "Shipment Creation Confirmation", generateShipmentConfirmationEmailContentString(savedShipment.getShipmentIdentifier().getShipmentId()));
        return shipmentResponseMapper.entityToResponseModel(savedShipment);
    }

    @Override
    public List<ShipmentResponseModel> getAllShipments(Optional<String> userId, Optional<String> email) {
        List<Shipment> shipments;

        if (userId.isPresent()) {
            shipments = shipmentRepository.findShipmentByUserId(userId.get());

        }
        else if (email.isPresent()) {
            shipments = shipmentRepository.findShipmentByEmail(email.get());

        }else {
            shipments = shipmentRepository.findAll();
        }

        return shipments.stream()
                .map(shipmentResponseMapper::entityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    public ShipmentResponseModel getShipment(String shipmentId) {
        Shipment existingShipment=shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
        if(existingShipment==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        return shipmentResponseMapper.entityToResponseModel(existingShipment);
    }

    @Override
    public EventResponseModel cancelShipment(String shipmentId) {
        Shipment existingShipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
        if(existingShipment == null){
            throw new ShipmentNotFoundException("shipmentId not found: "+ shipmentId);
        }
        if(existingShipment.getStatus() == Status.TRANSIT){
            throw new InvalidOperationException("Shipment cannot be cancelled while it is in transit.");
        }
        else if(existingShipment.getStatus() == Status.DELIVERED){
            throw new InvalidOperationException("Shipment cannot be cancelled after it has been delivered.");
        }
        else if(existingShipment.getStatus() == Status.CANCELED){
            throw new InvalidOperationException("Shipment has already been cancelled.");
        }
        else{
            existingShipment.setStatus(Status.CANCELED);
            shipmentRepository.save(existingShipment);
            emailUtil.SslEmail(existingShipment.getEmail(), "Shipment Cancellation Confirmation", generateCancelShipmentEmailContentString(existingShipment));
            return EventResponseModel.builder()
                    .resultType("SUCCESS")
                    .event("Shipment Cancelled")
                    .href(WebMvcLinkBuilder
                            .linkTo(ShipmentController.class)
                            .slash(shipmentId)
                            .withSelfRel()
                            .getHref())
                    .build();
        }
    }

    @Override
    public ShipmentResponseModel getShipmentDetailsByObserverCode(String observerCode) {
        Observer observer = observerRepository.findObserverByObserverCode(observerCode);
        if(observer == null){
            throw new ShipmentNotFoundException("Observer with code " + observerCode + " not found");
        }

        String shipmentId = observer.getShipmentIdentifier().getShipmentId();
        Shipment existingShipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
        if(existingShipment == null){
            throw new ShipmentNotFoundException("Shipment not found for observer code: " + observerCode);
        }

        return shipmentResponseMapper.entityToResponseModel(existingShipment);
    }

    @Override
    public List<ShipmentResponseModel> getShipmentsByDriverId(String driverId) {
        if(userService.getUserByUserId(driverId) == null)
            throw new UserNotFoundException("Driver with id " + driverId + " not found");
        List<Shipment> foundShipments=shipmentRepository.findAllByDriverId(driverId);
        return foundShipments.stream()
                .map(shipmentResponseMapper::entityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    public ShipmentObserversInventoriesItemsResponseModel getShipmentDetails(String shipmentId) {
        ShipmentDetailsObjects shipmentDetails = getShipmentDetailsWithoutObserverCodes(shipmentId);
        List<ObserverResponseModel> observers = observerService.getAllObservers(shipmentId);
        return shipmentObserversInventoriesItemsResponseMapper
                .mapShipmentInventoryItemsObserversToResponseModel(
                        shipmentDetails.shipmentResponseModel,
                        shipmentDetails.inventoryItemsResponseModels,
                        observers
                );
    }

    @Override
    public ShipmentInventoriesItemsResponseModel getShipmentDetailsForDriverReport(String shipmentId) {
        ShipmentDetailsObjects shipmentDetails = getShipmentDetailsWithoutObserverCodes(shipmentId);
        return shipmentInventoriesItemsResponseMapper
                .mapShipmentInventoryItemsToResponseModel(
                        shipmentDetails.shipmentResponseModel,
                        shipmentDetails.inventoryItemsResponseModels
                );
    }

    @Generated
    private ShipmentDetailsObjects getShipmentDetailsWithoutObserverCodes(String shipmentId) {
        Shipment existingShipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
        if(existingShipment == null){
            throw new ShipmentNotFoundException("Shipment not found for shipmentId: " + shipmentId);
        }
        List<InventoryResponseModel> inventories = inventoryService.getInventories(shipmentId, Optional.empty());
        List<InventoryItemsResponseModel> inventoryItems = inventories.stream()
                .map(inventory -> {
                    List<ItemResponseModel> items = itemService.getItems(shipmentId, inventory.getInventoryId(), Optional.empty());
                    return inventoryItemsResponseMapper.mapInventoryAndItemsListToResponseModel(inventory, items);
                })
                .collect(Collectors.toList());
        ShipmentResponseModel shipmentResponse = shipmentResponseMapper.entityToResponseModel(existingShipment);
        return new ShipmentDetailsObjects(shipmentResponse, inventoryItems);
    }
    
    @Override
    public List<ShipmentResponseModel> getUnassignedShipments() {
        return shipmentResponseMapper.entitiesToResponseModel(shipmentRepository.findAllByDriverIdIsNull());
    }

    @Override
    public ShipmentResponseModel assignTruckAndDriverToShipment(AssignDriverAndTruckToShipmentRequest assignDriverAndTruckToShipmentRequest) {

        Shipment shipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(assignDriverAndTruckToShipmentRequest.getShipmentId());

        if(shipment == null){
            throw new ShipmentNotFoundException("Shipment with id " + assignDriverAndTruckToShipmentRequest.getShipmentId() + " was not found.");
        } else {

            shipment.setDriverId(assignDriverAndTruckToShipmentRequest.getDriverId());
            shipment.setTruckIdentifier(assignDriverAndTruckToShipmentRequest.getVin());

            return shipmentResponseMapper.entityToResponseModel(shipmentRepository.save(shipment));
        }

    }

    @Override
    public ShipmentResponseModel setShipmentFinalWeight(FinalWeightRequestModel finalWeightRequestModel) {

        Shipment shipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(finalWeightRequestModel.getShipmentId());

        if(shipment == null){
            throw new ShipmentNotFoundException("Shipment with id " + finalWeightRequestModel.getShipmentId() + " was not found.");
        } else {
            shipment.setWeight(finalWeightRequestModel.getFinalWeight());

            return shipmentResponseMapper.entityToResponseModel(shipmentRepository.save(shipment));
        }
    }

    @Override
    public ShipmentResponseModel unassignShipment(String shipmentId, String driverId) {
        if(userService.getUserByUserId(driverId) == null){
            throw new UserNotFoundException("driverId not found: " + driverId);
        }

        Shipment existingShipment=shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
        if(existingShipment==null){
            throw new ShipmentNotFoundException("shipmentId not found: "+shipmentId);
        }

        if(existingShipment.getStatus() == Status.LOADING){
            throw new InvalidOperationException("Shipment cannot be unassigned while it is loading.");
        }
        else if(existingShipment.getStatus() == Status.TRANSIT){
            throw new InvalidOperationException("Shipment cannot be unassigned while it is in transit.");
        }
        else if(existingShipment.getStatus() == Status.DELIVERED){
            throw new InvalidOperationException("Shipment cannot be unassigned after it has been delivered.");
        }

        existingShipment.setTruckIdentifier(null);
        existingShipment.setDriverId(null);

        return shipmentResponseMapper.entityToResponseModel(shipmentRepository.save(existingShipment));
    }

    @Override
    public ShipmentResponseModel updateShipmentStatus(String shipmentId, ShipmentRequestModel shipmentRequestModel) {
        Shipment shipment = shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipmentId);
        if(shipment == null){
            throw new ShipmentNotFoundException("Shipment with id " + shipmentId + " was not found.");
        } else {
            shipment.setStatus(shipmentRequestModel.getStatus());
            return shipmentResponseMapper.entityToResponseModel(shipmentRepository.save(shipment));
        }
    }
}
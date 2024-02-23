package com.example.backend.shipmentsubdomain.businesslayer.observer;

import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.observer.Observer;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverRepository;
import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import com.example.backend.shipmentsubdomain.datalayer.truck.TruckIdentifier;
import com.example.backend.shipmentsubdomain.datamapperlayer.observer.ObserverRequestMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.observer.ObserverResponseMapper;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverRequestModel;
import com.example.backend.shipmentsubdomain.presentationlayer.observer.ObserverResponseModel;
import com.example.backend.util.exceptions.InvalidInputException;
import com.example.backend.util.exceptions.ShipmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ObserverServiceImplUnitTest {
    @Mock
    private ObserverRepository observerRepository;
    @Mock
    private ObserverResponseMapper observerResponseMapper;
    @Mock
    private ObserverRequestMapper observerRequestMapper;
    @Mock
    private ShipmentRepository shipmentRepository;
    @InjectMocks
    private ObserverServiceImpl observerService;

    private Observer observer;
    private ObserverRequestModel observerRequestModel;
    private ObserverResponseModel observerResponseModel;
    private Observer readObserver;
    private Observer editObserver;
    private Observer fullObserver;
    private Shipment mockShipment;


    @BeforeEach
    void setUp() {
        mockShipment = buildShipment();
        readObserver = buildObserver("Read Observer", Permission.READ, "READ123");
        editObserver = buildObserver("Edit Observer", Permission.EDIT, "EDIT123");
        fullObserver = buildObserver("Full Observer", Permission.FULL, "FULL123");
    }


    @Test
    void createObserver_ShouldSucceed() {
        ObserverRequestModel requestModel = new ObserverRequestModel("Observer Name", Permission.READ, "OBS123");
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(mockShipment);
        when(observerRequestMapper.requestModelToEntity(any(ObserverRequestModel.class))).thenReturn(readObserver);
        when(observerRepository.save(any(Observer.class))).thenReturn(readObserver);
        when(observerResponseMapper.entityToResponseModel(any(Observer.class))).thenReturn(new ObserverResponseModel("observerId", "Observer Name", "OBS123", Permission.READ, "shipmentId"));

        ObserverResponseModel result = observerService.createObserver(requestModel, "shipmentId");

        assertNotNull(result);
        assertEquals("Observer Name", result.getName());
        assertEquals(Permission.READ, result.getPermission());
        assertEquals("OBS123", result.getObserverCode());
    }

    @Test
    void createObserver_WithNonExistentShipment_ShouldThrowException() {
        String nonExistentShipmentId = "nonExistentShipmentId";
        ObserverRequestModel request = new ObserverRequestModel("Observer Name", Permission.READ, "OBS123");

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(nonExistentShipmentId)).thenReturn(null);

        assertThrows(InvalidInputException.class, () -> observerService.createObserver(request, nonExistentShipmentId));
    }

    @Test
    void createObserver_WhenObserverCodeAlreadyExists_ShouldGenerateNewCode() {
        ObserverRequestModel requestModel = new ObserverRequestModel("Observer Name", Permission.READ, "OBS123");
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId("shipmentId")).thenReturn(mockShipment);
        when(observerRequestMapper.requestModelToEntity(requestModel)).thenReturn(readObserver);

        // First code generated exists
        when(observerRepository.existsByObserverCode(anyString())).thenReturn(true).thenReturn(false);

        // Observer saved with new code
        Observer savedObserver = new Observer();
        savedObserver.setObserverCode("NEWCODE");
        when(observerRepository.save(any(Observer.class))).thenReturn(savedObserver);

        when(observerResponseMapper.entityToResponseModel(savedObserver)).thenReturn(new ObserverResponseModel("observerId", "Observer Name", "NEWCODE", Permission.READ, "shipmentId"));

        ObserverResponseModel result = observerService.createObserver(requestModel, "shipmentId");

        assertNotNull(result);
        assertEquals("NEWCODE", result.getObserverCode());
        verify(observerRepository, atLeastOnce()).existsByObserverCode(anyString());
        verify(observerRepository).save(any(Observer.class));
    }

    @Test
    void getAllObserverCodes_WithValidShipmentId_ShouldSucceed() {
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(mockShipment);
        when(observerRepository.findAllByShipmentIdentifier_ShipmentId(anyString())).thenReturn(java.util.List.of(readObserver, editObserver, fullObserver));
        when(observerResponseMapper.entitiesToResponseModels(anyList())).thenReturn(java.util.List.of(
                new ObserverResponseModel("observerId", "Read Observer", "READ123", Permission.READ, "shipmentId"),
                new ObserverResponseModel("observerId", "Edit Observer", "EDIT123", Permission.EDIT, "shipmentId"),
                new ObserverResponseModel("observerId", "Full Observer", "FULL123", Permission.FULL, "shipmentId")
        ));

        java.util.List<ObserverResponseModel> result = observerService.getAllObservers("shipmentId");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Read Observer", result.get(0).getName());
        assertEquals("Edit Observer", result.get(1).getName());
        assertEquals("Full Observer", result.get(2).getName());
    }

    @Test
    void getAllObservers_WhenShipmentDoesNotExist_ShouldThrowException() {
        String invalidShipmentId = "invalidShipmentId";
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(invalidShipmentId)).thenReturn(null);

        Exception exception = assertThrows(ShipmentNotFoundException.class, () -> observerService.getAllObservers(invalidShipmentId));

        String expectedMessage = "shipmentId not found: " + invalidShipmentId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getObserverByObserverCode_WhenObserverExists_ShouldReturnObserver() {
        String validObserverCode = "JJJJK3";
        String validShipmentId = "LLLJ3K";

        // Set up the mock Observer entity
        Observer foundObserver = buildObserver("Observer Name", Permission.READ, validObserverCode);
        ObserverResponseModel expectedResponseModel =
                new ObserverResponseModel("observerId", "Observer Name", validObserverCode, Permission.READ, validShipmentId);

        // Mock the repository and mapper behavior
        when(observerRepository.existsByObserverCode(validObserverCode)).thenReturn(true);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(validShipmentId)).thenReturn(mockShipment);
        when(observerRepository.findObserverByObserverCode(validObserverCode)).thenReturn(foundObserver);
        when(observerResponseMapper.entityToResponseModel(foundObserver)).thenReturn(expectedResponseModel);

        // Call the method under test
        ObserverResponseModel actualResponseModel = observerService.getObserverByObserverCode(validShipmentId, validObserverCode);

        // Assertions
        assertNotNull(actualResponseModel);
        assertEquals(expectedResponseModel, actualResponseModel);
    }


    @Test
    void getObserverByObserverCode_WhenObserverDoesNotExist_ShouldThrowException() {
        String invalidObserverCode = "invalidObserverCode";
        when(observerRepository.existsByObserverCode(invalidObserverCode)).thenReturn(false);

        Exception exception = assertThrows(InvalidInputException.class, () -> observerService
                .getObserverByObserverCode("validShipmentId", invalidObserverCode));

        String expectedMessage = "Observer with code: " + invalidObserverCode + " does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getObserverByObserverCode_WhenShipmentDoesNotExist_ShouldThrowException() {
        String invalidShipmentId = "invalidShipmentId";
        when(observerRepository.existsByObserverCode("validObserverCode")).thenReturn(true);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(invalidShipmentId)).thenReturn(null);

        Exception exception = assertThrows(ShipmentNotFoundException.class, () -> observerService
                .getObserverByObserverCode(invalidShipmentId, "validObserverCode"));

        String expectedMessage = "shipmentId not found: " + invalidShipmentId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void editObserverPermission_WhenObserverExists_ShouldUpdatePermission() {
        // Arrange
        String observerId = "ObserverId123";
        ObserverRequestModel requestModel = new ObserverRequestModel("Observer Name", Permission.EDIT, observerId);
        Observer existingObserver = buildObserver("Observer Name", Permission.READ, observerId);
        when(observerRepository.findByObserverIdentifier_ObserverId(observerId)).thenReturn(existingObserver);

        Observer updatedObserver = buildObserver("Observer Name", Permission.EDIT, observerId);
        when(observerRepository.save(any(Observer.class))).thenReturn(updatedObserver);
        when(observerResponseMapper.entityToResponseModel(any(Observer.class)))
                .thenReturn(new ObserverResponseModel("observerId", "Observer Name",
                        observerId, Permission.EDIT, "shipmentId"));

        // Act
        ObserverResponseModel result = observerService.editObserverPermission(requestModel, observerId, "shipmentId");

        // Assert
        assertNotNull(result);
        assertEquals(Permission.EDIT, result.getPermission());
        verify(observerRepository).save(any(Observer.class));
    }

    @Test
    void editObserverPermission_WhenObserverDoesNotExist_ShouldThrowException() {
        // Arrange
        String invalidObserverId = "invalidId";
        ObserverRequestModel requestModel = new ObserverRequestModel("Observer Name", Permission.EDIT, invalidObserverId);
        when(observerRepository.findObserverByObserverCode(invalidObserverId)).thenReturn(null);

        // Act
        Exception exception = assertThrows(InvalidInputException.class, () ->
                observerService.editObserverPermission(requestModel, invalidObserverId, "shipmentId"));

        // Assert
        String expectedMessage = "Observer with Id: " + invalidObserverId + " does not exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    //-------------------------------------- Observer Permission Tests --------------------------------------
    @Test
    void hasReadPermission_WithValidObserver_ShouldReturnTrue() {
        when(observerRepository.findObserverByObserverCode(readObserver.getObserverCode())).thenReturn(readObserver);

        boolean result = observerService.hasReadPermission(readObserver.getObserverCode());

        assertTrue(result);
    }

    @Test
    void hasEditPermission_WithValidObserver_ShouldReturnTrue() {
        when(observerRepository.findObserverByObserverCode(editObserver.getObserverCode())).thenReturn(editObserver);

        boolean result = observerService.hasEditPermission(editObserver.getObserverCode());

        assertTrue(result);
    }

    @Test
    void hasFullPermission_WithValidObserver_ShouldReturnTrue() {
        when(observerRepository.findObserverByObserverCode(fullObserver.getObserverCode())).thenReturn(fullObserver);

        boolean result = observerService.hasFullPermission(fullObserver.getObserverCode());

        assertTrue(result);
    }

    @Test
    void hasPermission_WithInvalidObserver_ShouldReturnFalse() {
        when(observerRepository.findObserverByObserverCode("Invalid")).thenReturn(null);

        assertFalse(observerService.hasReadPermission("Invalid"));
        assertFalse(observerService.hasEditPermission("Invalid"));
        assertFalse(observerService.hasFullPermission("Invalid"));
    }


    @Test
    void hasReadPermission_WithObserverHavingDifferentPermission_ShouldReturnFalse() {
        when(observerRepository.findObserverByObserverCode(editObserver.getObserverCode())).thenReturn(editObserver);

        boolean result = observerService.hasReadPermission(editObserver.getObserverCode());

        assertFalse(result);
    }

    @Test
    void hasEditPermission_WithObserverHavingDifferentPermission_ShouldReturnFalse() {
        when(observerRepository.findObserverByObserverCode(fullObserver.getObserverCode())).thenReturn(fullObserver);

        boolean result = observerService.hasEditPermission(fullObserver.getObserverCode());

        assertFalse(result);
    }

    @Test
    void hasFullPermission_WithObserverHavingDifferentPermission_ShouldReturnFalse() {
        when(observerRepository.findObserverByObserverCode(readObserver.getObserverCode())).thenReturn(readObserver);

        boolean result = observerService.hasFullPermission(readObserver.getObserverCode());

        assertFalse(result);
    }


    // -------------------------------- Builder Related Methods --------------------------------
    private Shipment buildShipment() {
        Address pickupAddress = new Address();
        Address destinationAddress = new Address();
        TruckIdentifier truckIdentifier = new TruckIdentifier("1A8HW58268F113778");
        Status inventoryStatus = Status.QUOTED;
        return mockShipment = new Shipment(
                "user123",
                truckIdentifier,
                inventoryStatus,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 5),
                2000.0,
                "Sample Shipment",
                pickupAddress,
                destinationAddress,
                "email@example.com",
                "1234567890",230.0,
                null);
    }

    private Observer buildObserver(String name, Permission permission, String observerCode) {
        return Observer.builder()
                .observerIdentifier(new ObserverIdentifier())
                .name(name)
                .observerCode(observerCode)
                .permission(permission)
                .build();
    }

}
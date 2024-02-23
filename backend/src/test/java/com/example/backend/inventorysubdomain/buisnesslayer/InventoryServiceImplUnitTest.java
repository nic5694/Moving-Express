package com.example.backend.inventorysubdomain.buisnesslayer;

import com.example.backend.inventorysubdomain.buisnesslayer.Item.ItemService;
import com.example.backend.inventorysubdomain.buisnesslayer.inventory.InventoryServiceImpl;
import com.example.backend.inventorysubdomain.datalayer.Item.Type;
import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryIdentifier;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.inventorysubdomain.datamapperlayer.inventory.InventoryRequestMapper;
import com.example.backend.inventorysubdomain.datamapperlayer.inventory.InventoryResponseMapper;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.shipmentsubdomain.businesslayer.observer.ObserverServiceImpl;
import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.shipment.*;
import com.example.backend.shipmentsubdomain.datalayer.truck.TruckIdentifier;
import com.example.backend.util.exceptions.InvalidInputException;
import com.example.backend.util.exceptions.InvalidOperationException;
import com.example.backend.util.exceptions.InventoryNotFoundException;
import com.example.backend.util.exceptions.ShipmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class InventoryServiceImplUnitTest {
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private InventoryResponseMapper inventoryResponseMapper;
    @Mock
    private InventoryRequestMapper inventoryRequestMapper;
    @Mock
    private InventoryRepository inventoryRepository;
    @InjectMocks
    private InventoryServiceImpl inventoryService;
    @Mock
    private ObserverServiceImpl observerService;
    @Mock
    private ItemService itemService;
    private String dummyShipmentId;

    private String dummyDescription = "Kitchen inventory holding all the kitchen items.";
    private String dummyInventoryName = "Kitchen";

    private Inventory sampleInventory;
    private InventoryRequestModel requestInventory;
    private InventoryResponseModel responseInventory;
    private Shipment mockShipment;

    @BeforeEach
    void setUp() {
        Address pickupAddress = new Address();
        Address destinationAddress = new Address();
        TruckIdentifier truckIdentifier = new TruckIdentifier("1A8HW58268F113778");
        Status inventoryStatus = Status.QUOTED;
        mockShipment = new Shipment(
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
        // dummyShipmentId = mockShipment.getShipmentIdentifier().getShipmentId();
        sampleInventory = new Inventory(mockShipment.getShipmentIdentifier(), dummyInventoryName, dummyDescription, 100.0);
        requestInventory = InventoryRequestModel.builder()
                .description(sampleInventory.getDescription())
                .name(sampleInventory.getName())
                .build();
        responseInventory = InventoryResponseModel.builder()
                .inventoryId(sampleInventory.getInventoryIdentifier().getInventoryId())
                .description(sampleInventory.getDescription())
                .shipmentId(sampleInventory.getShipmentIdentifier().getShipmentId())
                .name(sampleInventory.getName())
                .build();

    }

    @Test
    void getAllInventoriesByShipmentId_ShouldSucceed() {
        Shipment shipment = buildShipment();
        Inventory inventory1 = buildInventory(shipment.getShipmentIdentifier());
        Inventory inventory2 = buildInventory2(shipment.getShipmentIdentifier());
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        inventories.add(inventory2);

        InventoryResponseModel inventoryResponseModel1 = buildInventoryResponseModel(inventory1.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        InventoryResponseModel inventoryResponseModel2 = buildInventoryResponseModel2(inventory2.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        List<InventoryResponseModel> inventoryResponseModels = new ArrayList<>();
        inventoryResponseModels.add(inventoryResponseModel1);
        inventoryResponseModels.add(inventoryResponseModel2);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findAllByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(inventories);
        when(inventoryResponseMapper.entitiesToResponseModels(inventories)).thenReturn(inventoryResponseModels);

        List<InventoryResponseModel> result = inventoryService.getInventories(shipment.getShipmentIdentifier().getShipmentId(), Optional.empty());

        assertEquals(2, result.size());
        assertEquals(inventoryResponseModel1, result.get(0));
        assertEquals(inventoryResponseModel2, result.get(1));
    }



    @Test
    void addInventoryWithValidShipmentIdAndBody_ShouldSucceed() {
        //Arrange
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(mockShipment.getShipmentIdentifier().getShipmentId())).thenReturn(mockShipment);
        when(inventoryRequestMapper.requestModelToEntity(requestInventory)).thenReturn(sampleInventory);
        when(inventoryResponseMapper.entityToResponseModel(sampleInventory)).thenReturn(responseInventory);
        when(inventoryRepository.save(sampleInventory)).thenReturn(sampleInventory);

        //Act
        InventoryResponseModel inventoryResponseModel = inventoryService.addInventory(requestInventory, mockShipment.getShipmentIdentifier().getShipmentId(), Optional.empty());

        //Assert
        assertThat(inventoryResponseModel.getInventoryId()).isEqualTo(responseInventory.getInventoryId());
        assertThat(inventoryResponseModel.getShipmentId()).isEqualTo(responseInventory.getShipmentId());
        assertThat(inventoryResponseModel.getName()).isEqualTo(responseInventory.getName());
        assertThat(inventoryResponseModel.getDescription()).isEqualTo(responseInventory.getDescription());
        verify(inventoryRepository, times(1)).save(sampleInventory);
        verify(shipmentRepository, times(1)).findShipmentByShipmentIdentifier_ShipmentId(mockShipment.getShipmentIdentifier().getShipmentId());
        verify(inventoryRequestMapper, times(1)).requestModelToEntity(requestInventory);
        verify(inventoryResponseMapper, times(1)).entityToResponseModel(sampleInventory);
    }

    @Test
    void deleteInventoryWithValidUuid_ShouldSucceed() {
        // Arrange
        String validUuid = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(validUuid)).thenReturn(new Inventory(new ShipmentIdentifier("shipmentId"), "name", "description", 100.0));

        // Act
        inventoryService.deleteInventory(validUuid, Optional.empty());

        // Assert
        verify(inventoryRepository, times(1)).findByInventoryIdentifier_InventoryId(validUuid);
    }

    @Test
    void editInventoryWithValidUuid_ShouldSucceed() {
        // Arrange
        String validUuid = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(validUuid)).thenReturn(new Inventory());

        // Act
        inventoryService.editInventory(requestInventory, validUuid, Optional.empty());

        // Assert
        verify(inventoryRepository, times(1)).findByInventoryIdentifier_InventoryId(validUuid);
    }

    @Test
    void editInventoryWithInvalidInventoryId_ShouldThrowNotFoundException() {
        // Arrange
        String invalidInventoryId = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(invalidInventoryId)).thenReturn(null);

        // Act & Assert
        assertThatExceptionOfType(InventoryNotFoundException.class)
                .isThrownBy(() -> inventoryService.editInventory(requestInventory, invalidInventoryId, Optional.empty()))
                .withMessageContaining("Inventory with the id: " + invalidInventoryId + " does not exist.");
    }

    @Test
    void deleteInventoryWithNonExistingUuid_ShouldThrowNotFoundException() {
        // Arrange
        String nonExistingUuid = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(nonExistingUuid)).thenReturn(null);

        // Act & Assert
        assertThatExceptionOfType(InventoryNotFoundException.class)
                .isThrownBy(() -> inventoryService.deleteInventory(nonExistingUuid, Optional.empty()))
                .withMessageContaining("Inventory with the id: " + nonExistingUuid + " does not exist.");
    }

    @Test
    void addInventoryWithInvalidBodyAndValidShipmentId_ShouldThrowBadRequestException() {
        //Arrange
        Mockito.when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(mockShipment.getShipmentIdentifier().getShipmentId())).thenReturn(mockShipment);
        Mockito.when(inventoryRequestMapper.requestModelToEntity(requestInventory)).thenReturn(sampleInventory);
        Mockito.when(inventoryResponseMapper.entityToResponseModel(sampleInventory)).thenReturn(responseInventory);
        Mockito.when(inventoryRepository.save(sampleInventory)).thenReturn(sampleInventory);

        //Act & Assert
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> inventoryService.addInventory(new InventoryRequestModel(null, "description", InventoryStatus.CREATED), mockShipment.getShipmentIdentifier().getShipmentId(), Optional.empty()))
                .withMessageContaining("Request to addInventory to shipmentId: " + mockShipment.getShipmentIdentifier().getShipmentId() + " is invalid");
    }
    @Test
    void addInventoryWithInvalidBodyAndValidShipmentId_InvalidInputException() {
        //Act & Assert
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> inventoryService.addInventory(new InventoryRequestModel(" ", "description", InventoryStatus.CREATED), mockShipment.getShipmentIdentifier().getShipmentId(), Optional.empty()))
                .withMessageContaining("Request to addInventory to shipmentId: " + mockShipment.getShipmentIdentifier().getShipmentId() + " is invalid");
    }

    @Test
    void addInventoryWithValidBodyAndInvalidShipmentId_ShouldThrowNotFoundException() {
        //Arrange
        Mockito.when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(mockShipment.getShipmentIdentifier().getShipmentId())).thenReturn(null);
        Mockito.when(inventoryRequestMapper.requestModelToEntity(requestInventory)).thenReturn(sampleInventory);
        Mockito.when(inventoryResponseMapper.entityToResponseModel(sampleInventory)).thenReturn(responseInventory);
        Mockito.when(inventoryRepository.save(sampleInventory)).thenReturn(sampleInventory);

        //Act & Assert
        assertThatExceptionOfType(ShipmentNotFoundException.class)
                .isThrownBy(() -> inventoryService.addInventory(requestInventory, mockShipment.getShipmentIdentifier().getShipmentId(), Optional.empty()))
                .withMessageContaining("Shipmentid with the id: " + mockShipment.getShipmentIdentifier().getShipmentId() + " does not exist.");
    }

    @Test
    void addInventoryWithValidBodyAndEmptyShipmentId_ShouldInvalidInputException() {
        //Act & Assert
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> inventoryService.addInventory(requestInventory, "", Optional.empty()))
                .withMessageContaining("Request to addInventory to shipmentId: " + "" + " is invalid");
    }
    @Test
    void addInventoryWithValidBodyAndBlankShipmentId_ShouldInvalidInputException() {
        //Act & Assert
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> inventoryService.addInventory(requestInventory, " ", Optional.empty()))
                .withMessageContaining("Request to addInventory to shipmentId: " + " " + " is invalid");
    }

    // -------------------------------- Observer Related Methods --------------------------------
    @Test
    void getInventoriesWithValidObserverCodeAndReadPermission_ShouldSucceed() {
        // Arrange
        String validObserverCode = "ABC123";
        when(observerService.hasReadPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory1 = buildInventory(shipment.getShipmentIdentifier());
        Inventory inventory2 = buildInventory2(shipment.getShipmentIdentifier());
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        inventories.add(inventory2);

        InventoryResponseModel inventoryResponseModel1 = buildInventoryResponseModel(inventory1.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        InventoryResponseModel inventoryResponseModel2 = buildInventoryResponseModel2(inventory2.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        List<InventoryResponseModel> inventoryResponseModels = new ArrayList<>();
        inventoryResponseModels.add(inventoryResponseModel1);
        inventoryResponseModels.add(inventoryResponseModel2);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findAllByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(inventories);
        when(inventoryResponseMapper.entitiesToResponseModels(inventories)).thenReturn(inventoryResponseModels);

        // Act
        List<InventoryResponseModel> result = inventoryService.getInventories(shipment.getShipmentIdentifier().getShipmentId(), Optional.of(validObserverCode));


        // Assert
        assertEquals(2, result.size());
        assertEquals(inventoryResponseModel1, result.get(0));
        assertEquals(inventoryResponseModel2, result.get(1));
    }

    @Test
    void getInventoriesWithValidObserverCodeAndEditPermission_ShouldSucceed() {
        // Arrange
        String validObserverCode = "ABC123";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory1 = buildInventory(shipment.getShipmentIdentifier());
        Inventory inventory2 = buildInventory2(shipment.getShipmentIdentifier());
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        inventories.add(inventory2);

        InventoryResponseModel inventoryResponseModel1 = buildInventoryResponseModel(inventory1.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        InventoryResponseModel inventoryResponseModel2 = buildInventoryResponseModel2(inventory2.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        List<InventoryResponseModel> inventoryResponseModels = new ArrayList<>();
        inventoryResponseModels.add(inventoryResponseModel1);
        inventoryResponseModels.add(inventoryResponseModel2);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findAllByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(inventories);
        when(inventoryResponseMapper.entitiesToResponseModels(inventories)).thenReturn(inventoryResponseModels);

        // Act
        List<InventoryResponseModel> result = inventoryService.getInventories(shipment.getShipmentIdentifier().getShipmentId(), Optional.of(validObserverCode));


        // Assert
        assertEquals(2, result.size());
        assertEquals(inventoryResponseModel1, result.get(0));
        assertEquals(inventoryResponseModel2, result.get(1));
    }

    @Test
    void getInventoriesWithValidObserverCodeAndFullPermission_ShouldSucceed() {
        // Arrange
        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(true);

        Shipment shipment = buildShipment();
        Inventory inventory1 = buildInventory(shipment.getShipmentIdentifier());
        Inventory inventory2 = buildInventory2(shipment.getShipmentIdentifier());
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        inventories.add(inventory2);

        InventoryResponseModel inventoryResponseModel1 = buildInventoryResponseModel(inventory1.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        InventoryResponseModel inventoryResponseModel2 = buildInventoryResponseModel2(inventory2.getInventoryIdentifier().getInventoryId(), shipment.getShipmentIdentifier().getShipmentId());
        List<InventoryResponseModel> inventoryResponseModels = new ArrayList<>();
        inventoryResponseModels.add(inventoryResponseModel1);
        inventoryResponseModels.add(inventoryResponseModel2);

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(inventoryRepository.findAllByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(inventories);
        when(inventoryResponseMapper.entitiesToResponseModels(inventories)).thenReturn(inventoryResponseModels);

        // Act
        List<InventoryResponseModel> result = inventoryService.getInventories(shipment.getShipmentIdentifier().getShipmentId(), Optional.of(validObserverCode));


        // Assert
        assertEquals(2, result.size());
        assertEquals(inventoryResponseModel1, result.get(0));
        assertEquals(inventoryResponseModel2, result.get(1));
    }

    @Test
    void editInventoryWithValidObserverCodeAndUuid_ShouldSucceed() {
        // Arrange
        String validUuid = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(validUuid)).thenReturn(new Inventory());

        String validObserverCode = "ABC123";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(true);

        // Act
        inventoryService.editInventory(requestInventory, validUuid, Optional.of(validObserverCode));

        // Assert
        verify(inventoryRepository, times(1)).findByInventoryIdentifier_InventoryId(validUuid);
    }

    @Test
    void editInventoryWithValidObserverCodeWithFullPermissionAndUuid_ShouldSucceed() {
        // Arrange
        String validUuid = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(validUuid)).thenReturn(new Inventory());

        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(true);

        // Act
        inventoryService.editInventory(requestInventory, validUuid, Optional.of(validObserverCode));

        // Assert
        verify(inventoryRepository, times(1)).findByInventoryIdentifier_InventoryId(validUuid);
    }

    @Test
    void deleteInventoryWithValidObserverCodeAndUuid_ShouldSucceed() {
        // Arrange
        String validUuid = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(validUuid)).thenReturn(new Inventory(new ShipmentIdentifier("shipmentId"), "name", "description", 100.0));

        String validObserverCode = "ABC123";
        when(observerService.hasFullPermission(validObserverCode)).thenReturn(true);
        when(itemService.getItems("shipmentId","inventoryId", Optional.of(validObserverCode))).thenReturn(List.of(new ItemResponseModel("", "", Type.BOX,new BigDecimal(0),"",0.0,"","")));
        // Act
        inventoryService.deleteInventory(validUuid, Optional.of(validObserverCode));

        // Assert
        verify(inventoryRepository, times(1)).findByInventoryIdentifier_InventoryId(validUuid);
    }

    @Test
    void getInventoriesWithInvalidObserverCode_ShouldThrowInvalidInputException() {
        // Arrange
        String invalidObserverCode = "ABC12";

        // Act & Assert
        assertThatExceptionOfType(InvalidInputException.class)
                .isThrownBy(() -> inventoryService.getInventories(dummyShipmentId, Optional.of(invalidObserverCode)))
                .withMessageContaining("Invalid Observer Code: Observer codes must be 6 characters long.");
    }

    @Test
    void getInventoriesWithValidObserverCodeButNoReadPermission_ShouldThrowInvalidOperationException() {
        // Arrange
        String validObserverCode = "ABC123";
        when(observerService.hasReadPermission(validObserverCode)).thenReturn(false);

        // Act & Assert
        assertThatExceptionOfType(InvalidOperationException.class)
                .isThrownBy(() -> inventoryService.getInventories(dummyShipmentId, Optional.of(validObserverCode)))
                .withMessageContaining("Observer lacks READ permission");
    }
    @Test
    void editInventoryWithObserverHavingOnlyReadPermission_ShouldThrowInvalidOperationException() {
        // Arrange
        String validObserverCode = "ABC123";
        when(observerService.hasEditPermission(validObserverCode)).thenReturn(false);
        when(observerService.hasReadPermission(validObserverCode)).thenReturn(true);

        String validUuid = UUID.randomUUID().toString();
        Mockito.when(inventoryRepository.findByInventoryIdentifier_InventoryId(validUuid)).thenReturn(new Inventory());

        // Act & Assert
        assertThatExceptionOfType(InvalidOperationException.class)
                .isThrownBy(() -> inventoryService.editInventory(requestInventory, validUuid, Optional.of(validObserverCode)))
                .withMessageContaining("Observer lacks EDIT permission");
    }
    
    // -------------------------------- Builder Related Methods --------------------------------
    private Shipment buildShipment() {
        return Shipment.builder()
                .userId("exampleUserId")
                .status(Status.QUOTED)
                .shipmentIdentifier(new ShipmentIdentifier())
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .actualMovingDate(LocalDate.of(2023, 1, 5))
                .approximateWeight(100.0)
                .name("Example Shipment")
                .pickupAddress(Address.builder()
                        .city("PickupCity")
                        .streetAddress("PickupStreet")
                        .country(Country.CA)
                        .postalCode("PickupPostalCode")
                        .build())
                .destinationAddress(Address.builder()
                        .city("DestinationCity")
                        .streetAddress("DestinationStreet")
                        .country(Country.USA)
                        .postalCode("DestinationPostalCode")
                        .build())
                .email("example@example.com")
                .phoneNumber("1234567890")
                .build();
    }

    private Inventory buildInventory(ShipmentIdentifier shipmentIdentifier) {
        return Inventory.builder()
                .inventoryIdentifier(new InventoryIdentifier())
                .shipmentIdentifier(shipmentIdentifier)
                .name("Do's room")
                .description("This inventory holds the items in Do's room")
                .inventoryStatus(InventoryStatus.IN_PROGRESS)
                .approximateWeight(200.0)
                .build();
    }

    private InventoryResponseModel buildInventoryResponseModel(String inventoryId, String shipmentId) {
        return InventoryResponseModel.builder()
                .inventoryId(inventoryId)
                .shipmentId(shipmentId)
                .name("Do's room")
                .description("This inventory holds the items in Do's room")
                .inventoryStatus(InventoryStatus.IN_PROGRESS)
                .approximateWeight(200.0)
                .build();
    }

    private Inventory buildInventory2(ShipmentIdentifier shipmentIdentifier) {
        return Inventory.builder()
                .inventoryIdentifier(new InventoryIdentifier())
                .shipmentIdentifier(shipmentIdentifier)
                .name("Cal's room")
                .description("This inventory holds the items in Cal's room")
                .build();
    }

    private InventoryResponseModel buildInventoryResponseModel2(String inventoryId, String shipmentId) {
        return InventoryResponseModel.builder()
                .inventoryId(inventoryId)
                .shipmentId(shipmentId)
                .name("Cal's room")
                .description("This inventory holds the items in Cal's room")
                .build();

    }
}
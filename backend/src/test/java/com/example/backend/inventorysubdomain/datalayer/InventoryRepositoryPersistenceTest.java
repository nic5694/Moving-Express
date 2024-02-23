package com.example.backend.inventorysubdomain.datalayer;

import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryIdentifier;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import org.junit.jupiter.api.AfterEach;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Shipment;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryPersistenceTest {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    ShipmentRepository shipmentRepository;
    private Shipment shipment1;
    private Inventory inventory1;
    private Inventory inventory2;
    private Inventory testInventory;
    private String testInventoryId;

    @BeforeEach
    public void setUp(){
        inventoryRepository.deleteAll();
        shipment1=buildShipment();
        inventory1=buildInventory(shipment1.getShipmentIdentifier());
        inventory2=buildInventory2(shipment1.getShipmentIdentifier());

        ShipmentIdentifier shipmentId = new ShipmentIdentifier(UUID.randomUUID().toString());
        testInventory = new Inventory(shipmentId, "Sample Item", "Description of Sample Item", 120.0);
        testInventory = inventoryRepository.save(testInventory);
        testInventoryId = testInventory.getInventoryIdentifier().getInventoryId();

        inventoryRepository.saveAll(Arrays.asList(inventory1, inventory2));
    }

    @AfterEach
    public void tearDown() {
        inventoryRepository.deleteAll();
    }

    @Test
    public void whenFindByInventoryIdentifier_InventoryId_thenReturnInventory() {
        // Act
        Inventory foundInventory = inventoryRepository.findByInventoryIdentifier_InventoryId(testInventoryId);

        // Assert
        assertNotNull(foundInventory);
        assertEquals(testInventory.getName(), foundInventory.getName());
        assertEquals(testInventory.getDescription(), foundInventory.getDescription());
        assertEquals(testInventory.getShipmentIdentifier().getShipmentId(), foundInventory.getShipmentIdentifier().getShipmentId());
    }

    @Test
    public void givenAnInventoryIsCreated_whenSaved_thenShouldPersistCorrectly() {
        // Arrange
        ShipmentIdentifier shipmentId = new ShipmentIdentifier(UUID.randomUUID().toString());
        Inventory inventory = new Inventory(shipmentId, "Living Room", "I am here now", 250.0);

        // Act
        Inventory savedInventory = inventoryRepository.save(inventory);

        // Assert
        assertNotNull(savedInventory);
        assertEquals("Living Room", savedInventory.getName());
        assertEquals("I am here now", savedInventory.getDescription());
        assertEquals(shipmentId.getShipmentId(), savedInventory.getShipmentIdentifier().getShipmentId());
    }


    @Test
    public void whenFindAllInventoriesByValidShipmentId_thenReturnAllQuotes(){
        int expectedInventoryNumber=2;

        List<Inventory> inventories=inventoryRepository.findAllByShipmentIdentifier_ShipmentId(shipment1.getShipmentIdentifier().getShipmentId());

        assertEquals(expectedInventoryNumber, inventories.size());
        assertEquals(inventory1, inventories.get(0));
        assertEquals(inventory2, inventories.get(1));
    }

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

    private Inventory buildInventory(ShipmentIdentifier shipmentIdentifier){
        return Inventory.builder()
                .inventoryIdentifier(new InventoryIdentifier())
                .shipmentIdentifier(shipmentIdentifier)
                .name("Do's room")
                .description("This inventory holds the items in Do's room")
                .build();
    }

    private InventoryResponseModel buildInventoryResponseModel(String inventoryId, String shipmentId){
        return InventoryResponseModel.builder()
                .inventoryId(inventoryId)
                .shipmentId(shipmentId)
                .name("Do's room")
                .description("This inventory holds the items in Do's room")
                .build();
    }

    private Inventory buildInventory2(ShipmentIdentifier shipmentIdentifier){
        return Inventory.builder()
                .inventoryIdentifier(new InventoryIdentifier())
                .shipmentIdentifier(shipmentIdentifier)
                .name("Cal's room")
                .description("This inventory holds the items in Cal's room")
                .build();
    }

    private InventoryResponseModel buildInventoryResponseModel2(String inventoryId, String shipmentId){
        return InventoryResponseModel.builder()
                .inventoryId(inventoryId)
                .shipmentId(shipmentId)
                .name("Cal's room")
                .description("This inventory holds the items in Cal's room")
                .build();
    }
}
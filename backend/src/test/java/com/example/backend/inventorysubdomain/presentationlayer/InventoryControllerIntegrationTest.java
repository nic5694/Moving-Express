package com.example.backend.inventorysubdomain.presentationlayer;

import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc()
@ActiveProfiles("test")
@Sql({"/data-h2.sql"})
class InventoryControllerIntegrationTest {

    private static final String BASE_URI_INVENTORIES = "/api/v1/movingexpress/shipments/";
    private static final String INVENTORY_PATH = "/inventories/";
    private final String VALID_SHIPMENT_ID="c0a80121-7f5e-4d77-a5a4-5d41b04f5a57";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InventoryRepository inventoryRepository;

    private Inventory testInventory;
    private String testShipmentId = "c0a80121-7f5e-4d77-a5a4-5d41b04f5a57";
    private String testInventoryId;
    private InventoryRequestModel requestInventory;

    @BeforeEach
    void setUp() {
        testInventory = new Inventory(new ShipmentIdentifier(testShipmentId), "TestInventory", "Test Description", 130.0);
        testInventory = inventoryRepository.save(testInventory);
        testInventoryId = testInventory.getInventoryIdentifier().getInventoryId();
        requestInventory = InventoryRequestModel.builder()
                .description("Kitchen description")
                .name("Kitchen")
                .build();
    }

    @AfterEach
    void tearDown() {
        inventoryRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void WhenGetInventoryByID_ThenReturnInventory(){

        webTestClient.get()
                .uri(BASE_URI_INVENTORIES+testShipmentId+"/inventories/"+testInventoryId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(InventoryResponseModel.class)
                .consumeWith( response -> {
                    InventoryResponseModel inventoryResponseModel = response.getResponseBody();
                    assert inventoryResponseModel != null;
                    assertEquals(inventoryResponseModel.getName(),testInventory.getName());
                });

    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetInventoryByInvalidID_thenReturnInventoryNotFound() {

        String invalidInventoryID = "123";

        webTestClient.get()
                .uri(BASE_URI_INVENTORIES + testShipmentId + "/inventories/" + invalidInventoryID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Inventory with inventory id: " + invalidInventoryID + " not found");
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void WhenGetAllInventoriesByShipmentId_ThenReturnAllInventories() {
        int expectedSize = 3;

        String[] expectedNames = {"Kitchen", "Bathroom"};
        String[] expectedInventoryIds = {"550e8400-e29b-41d4-a716-446655440000", "8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f361a"};
        String[] expectedDescriptions = {"This is the inventory for the kitchen", "This is the inventory for the bathroom"};
        String[] expectedShipmentIds = {"c0a80121-7f5e-4d77-a5a4-5d41b04f5a57", "c0a80121-7f5e-4d77-a5a4-5d41b04f5a57"};
        InventoryStatus[] expectedStatuses = {InventoryStatus.CREATED, InventoryStatus.PACKED};
        int[] expectedWeights = {150, 180};

        webTestClient.get()
                .uri(BASE_URI_INVENTORIES+VALID_SHIPMENT_ID+"/inventories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InventoryResponseModel.class)
                .consumeWith(response -> {
                    List<InventoryResponseModel> inventoryResponseModels = response.getResponseBody();
                    assertThat(inventoryResponseModels).isNotEmpty();
                    assertEquals(inventoryResponseModels.size(), expectedSize);

                    for (int i = 0; i < 2; i++) {
                        InventoryResponseModel inventory = inventoryResponseModels.get(i);

                        assertEquals(inventory.getName(), expectedNames[i]);
                        assertEquals(inventory.getInventoryId(), expectedInventoryIds[i]);
                        assertEquals(inventory.getDescription(), expectedDescriptions[i]);
                        assertEquals(inventory.getShipmentId(), expectedShipmentIds[i]);
                        assertEquals(inventory.getInventoryStatus(), expectedStatuses[i]);
                        assertEquals(inventory.getApproximateWeight(), expectedWeights[i]);
                    }
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetInventoriesWithInvalidShipmentId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId = "123";

        webTestClient.get()
                .uri(BASE_URI_INVENTORIES + invalidShipmentId+"/inventories")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    void updateInventory_whenInventoryExists_thenOk() {
        String putUri = BASE_URI_INVENTORIES + testShipmentId + INVENTORY_PATH + testInventoryId;
        InventoryRequestModel inventoryRequestModel = new InventoryRequestModel("New Name", "New Description" , InventoryStatus.CREATED);

        webTestClient.put()
                .uri(putUri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(inventoryRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(inventoryRequestModel.getName())
                .jsonPath("$.description").isEqualTo(inventoryRequestModel.getDescription());
    }
    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    void deleteInventory_whenInventoryExists_thenNoContent() {
        String deleteUri = BASE_URI_INVENTORIES + testShipmentId + INVENTORY_PATH + testInventoryId;

        webTestClient.delete()
                .uri(deleteUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        boolean exists = inventoryRepository.existsById(testInventory.getId());
        assertFalse(exists);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void WhenAddInventoryWithValidFields_ThenReturnStatus201(){
        webTestClient.post()
                .uri(BASE_URI_INVENTORIES + testShipmentId + "/inventories")
                .bodyValue(requestInventory)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(InventoryResponseModel.class)
                .value(inventoryResponseModel -> {
                    assertEquals(inventoryResponseModel.getShipmentId(), testShipmentId);
                    assertEquals(inventoryResponseModel.getDescription(), requestInventory.getDescription());
                    assertNotNull(inventoryResponseModel.getInventoryId());
                });
    }

}
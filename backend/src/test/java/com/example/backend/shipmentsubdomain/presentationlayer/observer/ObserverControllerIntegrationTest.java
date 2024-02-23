package com.example.backend.shipmentsubdomain.presentationlayer.observer;

import com.example.backend.inventorysubdomain.datalayer.inventory.Inventory;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryRepository;
import com.example.backend.inventorysubdomain.datalayer.inventory.InventoryStatus;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemIdentifier;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemRepository;
import com.example.backend.inventorysubdomain.datalayer.Item.Type;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.inventory.InventoryResponseModel;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemRequestModel;
import com.example.backend.inventorysubdomain.presentationlayer.Item.ItemResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.observer.Observer;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverIdentifier;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverRepository;
import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentIdentifier;
import com.example.backend.util.exceptions.ObserverCodeNotFound;
import com.example.backend.util.exceptions.ShipmentNotFoundException;
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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Sql({"/data-h2.sql"})
class ObserverControllerIntegrationTest {
    private static final String BASE_URI_OBSERVERS = "/api/v1/movingexpress/shipments/";
    private static final String OBSERVER_PATH = "/observers";
    private final String VALID_SHIPMENT_ID="c0a80121-7f5e-4d77-a5a4-5d41b04f5a57";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObserverRepository observerRepository;

    private Observer testObserver;
    private ObserverRequestModel observerRequestModel;
    private ObserverResponseModel observerResponseModel;
    @Autowired
    private InventoryRepository inventoryRepository;
    private String testShipmentId = "c0a80121-7f5e-4d77-a5a4-5d41b04f5a57";
    private Inventory testInventory;
    private String testInventoryId;
    private InventoryRequestModel requestInventory;
    private ItemRequestModel itemRequestModel;
    private ItemResponseModel itemResponseModel;
    @Autowired
    private ItemRepository itemRepository;
    private final String URI_INVENTORIES="inventories";
    private final String VALID_INVENTORY_ID="550e8400-e29b-41d4-a716-446655440000";
    private final String URI_ITEMS="items";
    private ItemRequestModel buildItemRequestModel(){
        return ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();
    }

    private ItemResponseModel buildItemResponseModel(){
        return ItemResponseModel.builder()
                .inventoryId("550e8400-e29b-41d4-a716-446655440000")
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();
    }
    @BeforeEach
    void setUp() {
        observerRequestModel = ObserverRequestModel.builder()
                .name("Test Observer")
                .permission(Permission.READ)
                .observerCode("OBS123")
                .build();
        testInventory = new Inventory(new ShipmentIdentifier(testShipmentId), "TestInventory", "Test Description", 130.0);
        testInventory = inventoryRepository.save(testInventory);
        testInventoryId = testInventory.getInventoryIdentifier().getInventoryId();
        requestInventory = InventoryRequestModel.builder()
                .description("Kitchen description")
                .name("Kitchen")
                .build();
        itemRequestModel=buildItemRequestModel();
        itemResponseModel=buildItemResponseModel();
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    void createObserver_ShouldReturnCreatedObserver() {
        webTestClient.post()
                .uri(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(observerRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ObserverResponseModel.class)
                .consumeWith(response -> {
                    ObserverResponseModel responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals("Test Observer", responseBody.getName());
                    assertEquals(Permission.READ, responseBody.getPermission());
                    assertNotNull(responseBody.getObserverCode());
                    assertEquals(VALID_SHIPMENT_ID, responseBody.getShipmentId());
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    void getAllObservers_ShouldReturnListOfObservers() {
        String shipmentIdWithObservers = "c0a80121-7f5e-4d77-a5a4-5d41b04f5a23";

        webTestClient.get()
                .uri(BASE_URI_OBSERVERS + shipmentIdWithObservers + OBSERVER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ObserverResponseModel.class)
                .consumeWith(response -> {
                    List<ObserverResponseModel> responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertFalse(responseBody.isEmpty());

                    ObserverResponseModel observer = responseBody.stream()
                            .filter(o -> "MBS123".equals(o.getObserverCode()))
                            .findFirst()
                            .orElse(null);

                    assertNotNull(observer);
                    assertEquals("Dummy Observer", observer.getName());
                    assertEquals(Permission.FULL, observer.getPermission());
                    assertEquals("MBS123", observer.getObserverCode());
                    assertEquals(shipmentIdWithObservers, observer.getShipmentId());
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    void getObserverByObserverCode_ShouldReturnObserver() {
        String validObserverCode = "MBS123";

        webTestClient.get()
                .uri(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + validObserverCode)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ObserverResponseModel.class)
                .consumeWith(response -> {
                    ObserverResponseModel responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals(validObserverCode, responseBody.getObserverCode());
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void deleteObserverCode_shouldReturn200OK() {
        Observer observerCode = Observer.builder()
                .observerIdentifier(new ObserverIdentifier("c0a80121-7f5e-4d12-a5a4-5d41b04f5a23"))
                .name("Test")
                .permission(Permission.READ)
                .observerCode("123ABC")
                .shipmentIdentifier(new ShipmentIdentifier(VALID_SHIPMENT_ID))
                .build();

        observerRepository.save(observerCode);

        webTestClient.delete()
                .uri(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/c0a80121-7f5e-4d12-a5a4-5d41b04f5a23")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void deleteObserverCodeWithInvalidShipmentId_shouldReturnShipmentNotFound() {

        String invalidShipmentId = "invalidShipmentId";

        webTestClient.delete()
                .uri(BASE_URI_OBSERVERS + invalidShipmentId + OBSERVER_PATH + "/c0a80121-7f5e-4d12-a5a4-5d41b04f5a23")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ShipmentNotFoundException.class);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void deleteObserverCodeWithInvalidObserverCodeId_shouldReturnShipmentNotFound() {

        String invalidObserverCodeId = "invalidObserverCodeId";

        webTestClient.delete()
                .uri(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + invalidObserverCodeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ObserverCodeNotFound.class);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    void editObserverPermission_ShouldUpdatePermission() {
        String validObserverId = "456e7891-e89b-12d3-a456-426614174801";
        String shipmentIdForObserverId = "c0a80121-7f5e-4d77-a5a4-5d41b04f5a23";
        ObserverRequestModel updatedObserverRequest = new ObserverRequestModel("Dummy Observer",
                Permission.EDIT, validObserverId);

        webTestClient.put()
                .uri(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + validObserverId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedObserverRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ObserverResponseModel.class)
                .consumeWith(response -> {
                    ObserverResponseModel responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals("Dummy Observer", responseBody.getName());
                    assertEquals(Permission.EDIT, responseBody.getPermission());
                    assertEquals(validObserverId, responseBody.getObserverId());
                    assertEquals(shipmentIdForObserverId, responseBody.getShipmentId());
                });
    }

    @Test
    public void WhenGetInventoryByID_AndValidObserverCode_ThenReturnInventory(){
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/inventories/"+testInventoryId).queryParam("observerCode", "MBS123").build())
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
    public void whenGetInventoryByInvalidID_AndValidObserverCode_thenReturnInventoryNotFound() {
        String invalidInventoryID = "123";
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/inventories/"+invalidInventoryID).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Inventory with inventory id: " + invalidInventoryID + " not found");
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void WhenGetAllInventoriesByShipmentId_AndValidObserverCode_ThenReturnAllInventories() {
        int expectedSize = 3;

        String[] expectedNames = {"Kitchen", "Bathroom"};
        String[] expectedInventoryIds = {"550e8400-e29b-41d4-a716-446655440000", "8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f361a"};
        String[] expectedDescriptions = {"This is the inventory for the kitchen", "This is the inventory for the bathroom"};
        String[] expectedShipmentIds = {"c0a80121-7f5e-4d77-a5a4-5d41b04f5a57", "c0a80121-7f5e-4d77-a5a4-5d41b04f5a57"};
        InventoryStatus[] expectedStatuses = {InventoryStatus.CREATED, InventoryStatus.PACKED};
        int[] expectedWeights = {150, 180};

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/inventories").queryParam("observerCode", "MBS123").build())
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
    public void whenGetInventoriesWithInvalidShipmentId_AndValidObserverCode_thenReturnShipmentNotFoundException(){
        String invalidShipmentId = "123";

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + invalidShipmentId + OBSERVER_PATH + "/inventories").queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    void updateInventory_whenInventoryExists_AndValidObserverCode_thenOk() {
        String putUri = BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/inventories/" + testInventoryId;
        InventoryRequestModel inventoryRequestModel = new InventoryRequestModel("New Name", "New Description" , InventoryStatus.CREATED);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(putUri).queryParam("observerCode", "MBS123").build())
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
    void deleteInventory_whenInventoryExists_AndValidObserverCode_thenNoContent() {
        String deleteUri = BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/inventories/" + testInventoryId;

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(deleteUri).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        boolean exists = inventoryRepository.existsById(testInventory.getId());
        assertFalse(exists);
    }

    @Test
    public void WhenAddInventoryWithValidFields_AndValidObserverCode_ThenReturnStatus201(){
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/inventories").queryParam("observerCode", "MBS123").build())
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

    @Test
    public void whenAddItemWithValidValues_AndValidObserverCode_thenReturnNewItem(){
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+VALID_INVENTORY_ID+"/"+URI_ITEMS).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ItemResponseModel.class)
                .value((result)->{
                    assertNotNull(result);
                    assertNotNull(result.getItemId());
                    assertNotNull(result.getInventoryId());
                    assertEquals(itemResponseModel.getName(), result.getName());
                    assertEquals(itemResponseModel.getType(), result.getType());
                    assertEquals(itemResponseModel.getPrice(), result.getPrice());
                    assertEquals(itemResponseModel.getDescription(), result.getDescription());
                    assertEquals(itemResponseModel.getWeight(), result.getWeight());
                    assertEquals(itemResponseModel.getHandlingInstructions(), result.getHandlingInstructions());
                    assertEquals(itemResponseModel.getInventoryId(), result.getInventoryId());
                });
    }

    @Test
    public void whenAddItemWithInvalidShipmentId_AndValidObserverCode_thenReturnShipmentNotFoundException(){
        String invalidShipmentId="123";

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + invalidShipmentId + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+VALID_INVENTORY_ID+"/"+URI_ITEMS).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    public void whenAddItemWithInvalidType_AndValidObserverCode_thenReturnInvalidTypeException(){
        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(null)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+VALID_INVENTORY_ID+"/"+URI_ITEMS).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid type: " + itemRequestModel1.getType());
    }

    @Test
    public void whenAddItemToInventoryWithInvalidInventoryStatus_AndValidObserverCode_thenReturnInvalidInventoryStatusException(){
        String inventoryId="8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f361a";

        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+inventoryId+"/"+URI_ITEMS).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid inventoryStatus: PACKED");
    }

    @Test
    public void whenGetAllItemsByInventoryId_AndValidObserverCode_ThenReturnAllItems() {
        int expectedSize = 2;

        String[] expectedItemIds = {
                "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6",
                "q7r8s9t0-u1v2-w3x4-y5z6-a7b8c9d0e1f2"
        };
        String[] expectedNames = {"Widget A", "Box B"};
        Type[] expectedTypes = {Type.ITEM, Type.BOX};
        BigDecimal[] expectedPrices = {new BigDecimal("19.99"), new BigDecimal("39.99")};
        String[] expectedDescriptions = {
                "A high-quality widget",
                "Sturdy box for shipping"
        };
        Double[] expectedWeights = {0.5, 2.0};
        String[] expectedHandlingInstructions = {"Handle with care", "Fragile items inside"};
        String expectedInventoryId = "8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613";

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+"8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613"+"/"+URI_ITEMS).queryParam("observerCode", "MBS123").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ItemResponseModel.class)
                .consumeWith(response -> {
                    List<ItemResponseModel> itemResponseModels = response.getResponseBody();
                    assertThat(itemResponseModels).isNotEmpty();
                    assertEquals(itemResponseModels.size(), expectedSize);

                    for (int i = 0; i < expectedSize; i++) {
                        ItemResponseModel item = itemResponseModels.get(i);

                        assertEquals(item.getItemId(), expectedItemIds[i]);
                        assertEquals(item.getName(), expectedNames[i]);
                        assertEquals(item.getType(), expectedTypes[i]);
                        assertEquals(item.getPrice(), expectedPrices[i]);
                        assertEquals(item.getDescription(), expectedDescriptions[i]);
                        assertEquals(item.getWeight(), expectedWeights[i]);
                        assertEquals(item.getHandlingInstructions(), expectedHandlingInstructions[i]);
                        assertEquals(item.getInventoryId(), expectedInventoryId);
                    }
                });
    }

    @Test
    public void whenGetItemsWithInvalidShipmentId_AndValidObserverCode_thenReturnShipmentNotFoundException(){
        String invalidShipmentId = "123";

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + invalidShipmentId + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+"8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613"+"/"+URI_ITEMS).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    public void whenGetItemWithItemId_AndValidObserverCode_thenReturnItem(){
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+"8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613"+"/"+URI_ITEMS + "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6").queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("itemId").isEqualTo("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6")
                .jsonPath("name").isEqualTo("Widget A")
                .jsonPath("type").isEqualTo("ITEM")
                .jsonPath("price").isEqualTo(19.99)
                .jsonPath("description").isEqualTo("A high-quality widget")
                .jsonPath("weight").isEqualTo(0.5)
                .jsonPath("handlingInstructions").isEqualTo("Handle with care")
                .jsonPath("inventoryId").isEqualTo("8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613");
    }

    @Test
    public void whenGetItemWithInvalidShipmentId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId = "123";

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + invalidShipmentId + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+"8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613"+"/"+URI_ITEMS + "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6").queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    public void whenGetItemWithInvalidInventoryId_thenReturnInventoryNotFoundException(){
        String invalidInventoryId = "123";

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ invalidInventoryId + "/items/" + "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6").queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("inventoryId not found: " + invalidInventoryId);
    }

    @Test
    public void whenGetItemWithInvalidItemId_thenReturnItemNotFoundException(){
        String invalidItemId = "123";
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ VALID_INVENTORY_ID + "/items/" + invalidItemId).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("itemId not found: " + invalidItemId);
    }

    @Test
    public void whenDeleteItemWithItemId_thenReturnVoid(){
        String validItemId = "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ VALID_INVENTORY_ID + "/items/" + validItemId).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        assertEquals(0,
                itemRepository.findAllByInventoryIdentifier_InventoryId(VALID_INVENTORY_ID).size());
        assertNull(itemRepository.findByItemIdentifier_ItemId(validItemId));
    }

    @Test
    public void whenDeleteItemWithItemId_thenReturnShipmentNotFoundException(){
        String validItemId = "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";
        String invalidShipmentId = "123";

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + invalidShipmentId + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ VALID_INVENTORY_ID + "/items/" + validItemId).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    public void whenUpdateItemWithValidFields_thenReturnItem(){
        String validItemId = "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";

        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        ItemResponseModel itemResponseModel1=ItemResponseModel.builder()
                .itemId(new ItemIdentifier().getItemId())
                .inventoryId(VALID_INVENTORY_ID)
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ VALID_INVENTORY_ID + "/items/" + validItemId).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ItemResponseModel.class)
                .value((result)->{
                    assertNotNull(result);
                    assertNotNull(result.getItemId());
                    assertNotNull(result.getInventoryId());
                    assertEquals(itemResponseModel1.getName(), result.getName());
                    assertEquals(itemResponseModel1.getType(), result.getType());
                    assertEquals(itemResponseModel1.getPrice(), result.getPrice());
                    assertEquals(itemResponseModel1.getDescription(), result.getDescription());
                    assertEquals(itemResponseModel1.getWeight(), result.getWeight());
                    assertEquals(itemResponseModel1.getHandlingInstructions(), result.getHandlingInstructions());
                    assertEquals(itemResponseModel1.getInventoryId(), VALID_INVENTORY_ID);
                });
    }

    @Test
    public void whenUpdateItemWithInvalidShipmentId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId="123";
        String validItemId = "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";

        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + invalidShipmentId + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ VALID_INVENTORY_ID + "/items/" + validItemId).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    public void whenUpdateItemWithInvalidInventoryId_thenReturnInventoryNotFoundException(){
        String invalidInventoryId="123";
        String validItemId = "/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";

        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ invalidInventoryId + "/items/" + validItemId).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("inventoryId not found: " + invalidInventoryId);
    }

    @Test
    public void whenUpdateItemWithInvalidItemId_thenReturnItemNotFoundException(){
        String invalidItemId="123";

        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_OBSERVERS + VALID_SHIPMENT_ID + OBSERVER_PATH + "/" + URI_INVENTORIES+"/"+ VALID_INVENTORY_ID + "/items/" + invalidItemId).queryParam("observerCode", "MBS123").build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("itemId not found: " + invalidItemId);
    }
}
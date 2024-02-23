package com.example.backend.inventorysubdomain.presentationlayer.Item;

import com.example.backend.inventorysubdomain.datalayer.Item.ItemIdentifier;
import com.example.backend.inventorysubdomain.datalayer.Item.ItemRepository;
import com.example.backend.inventorysubdomain.datalayer.Item.Type;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Sql({"/data-h2.sql"})
class ItemControllerIntegrationTest {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ItemRepository itemRepository;

    private final String BASE_URI_SHIPMENTS="/api/v1/movingexpress/shipments";
    private final String VALID_SHIPMENT_ID="c0a80121-7f5e-4d77-a5a4-5d41b04f5a57";
    private final String URI_INVENTORIES="inventories";
    private final String VALID_INVENTORY_ID="550e8400-e29b-41d4-a716-446655440000";
    private final String URI_ITEMS="items";
    private final String EXISTING_URI_ITEMS="/api/v1/movingexpress/shipments/c0a80121-7f5e-4d77-a5a4-5d41b04f5a23/inventories/8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613/items";
    private final String VALID_ITEM_ID="a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";

    private ItemRequestModel itemRequestModel;
    private ItemResponseModel itemResponseModel;

    @BeforeEach
    public void setUp(){
        itemRequestModel=buildItemRequestModel();
        itemResponseModel=buildItemResponseModel();
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenAddItemWithValidValues_thenReturnNewItem(){
        webTestClient.post()
                .uri(BASE_URI_SHIPMENTS+"/"+VALID_SHIPMENT_ID+"/"+URI_INVENTORIES+"/"+VALID_INVENTORY_ID+"/"+URI_ITEMS)
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
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenAddItemWithInvalidShipmentId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId="123";

        webTestClient.post()
                .uri(BASE_URI_SHIPMENTS+"/"+invalidShipmentId+"/"+URI_INVENTORIES+"/"+VALID_INVENTORY_ID+"/"+URI_ITEMS)
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
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenAddItemWithInvalidInventoryId_thenReturnInventoryNotFoundException(){
        String invalidInventoryId="123";

        webTestClient.post()
                .uri(BASE_URI_SHIPMENTS+"/"+VALID_SHIPMENT_ID+"/"+URI_INVENTORIES+"/"+invalidInventoryId+"/"+URI_ITEMS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("inventoryId not found: " + invalidInventoryId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenAddItemWithInvalidType_thenReturnInvalidTypeException(){
        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(null)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.post()
                .uri(BASE_URI_SHIPMENTS+"/"+VALID_SHIPMENT_ID+"/"+URI_INVENTORIES+"/"+VALID_INVENTORY_ID+"/"+URI_ITEMS)
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
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenAddItemToInventoryWithInvalidInventoryStatus_thenReturnInvalidInventoryStatusException(){
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
                .uri(BASE_URI_SHIPMENTS+"/"+VALID_SHIPMENT_ID+"/"+URI_INVENTORIES+"/"+inventoryId+"/"+URI_ITEMS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequestModel1)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid inventoryStatus: PACKED");
    }

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

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetAllItemsByInventoryId_ThenReturnAllItems() {
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
                .uri(EXISTING_URI_ITEMS)
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
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetItemsWithInvalidShipmentId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId = "123";

        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS+"/" + invalidShipmentId+"/inventories/"+VALID_INVENTORY_ID+"/items")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetItemsWithInvalidInventoryId_thenReturnInventoryNotFoundException(){
        String invalidInventoryId = "123";

        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS+"/" + VALID_SHIPMENT_ID+"/inventories/"+invalidInventoryId+"/items")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("inventoryId not found: " + invalidInventoryId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetItemWithItemId_thenReturnItem(){
        webTestClient.get()
                .uri(EXISTING_URI_ITEMS+"/"+VALID_ITEM_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("itemId").isEqualTo(VALID_ITEM_ID)
                .jsonPath("name").isEqualTo("Widget A")
                .jsonPath("type").isEqualTo("ITEM")
                .jsonPath("price").isEqualTo(19.99)
                .jsonPath("description").isEqualTo("A high-quality widget")
                .jsonPath("weight").isEqualTo(0.5)
                .jsonPath("handlingInstructions").isEqualTo("Handle with care")
                .jsonPath("inventoryId").isEqualTo("8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613");
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetItemWithInvalidShipmentId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId = "123";

        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS+"/" + invalidShipmentId+"/inventories/"+VALID_INVENTORY_ID+"/items/"+VALID_ITEM_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetItemWithInvalidInventoryId_thenReturnInventoryNotFoundException(){
        String invalidInventoryId = "123";

        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS+"/" + VALID_SHIPMENT_ID+"/inventories/"+invalidInventoryId+"/items/"+VALID_INVENTORY_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("inventoryId not found: " + invalidInventoryId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetItemWithInvalidItemId_thenReturnItemNotFoundException(){
        String invalidItemId = "123";

        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS+"/" + VALID_SHIPMENT_ID+"/inventories/"+VALID_INVENTORY_ID+"/items/"+invalidItemId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("itemId not found: " + invalidItemId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenDeleteItemWithItemId_thenReturnVoid(){
        webTestClient.delete()
                .uri(EXISTING_URI_ITEMS+"/"+VALID_ITEM_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        assertEquals(1,
                itemRepository.findAllByInventoryIdentifier_InventoryId("8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613").size());
        assertNull(itemRepository.findByItemIdentifier_ItemId(VALID_ITEM_ID));
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenDeleteItemWithItemId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId = "123";

        webTestClient.delete()
                .uri(BASE_URI_SHIPMENTS+"/" + invalidShipmentId+"/inventories/"+VALID_INVENTORY_ID+"/items/"+VALID_ITEM_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenDeleteItemWithItemId_thenReturnInventoryNotFoundException(){
        String invalidInventoryId = "123";

        webTestClient.delete()
                .uri(BASE_URI_SHIPMENTS+"/" + VALID_SHIPMENT_ID+"/inventories/"+invalidInventoryId+"/items/"+VALID_INVENTORY_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("inventoryId not found: " + invalidInventoryId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenDeleteItemWithItemId_thenReturnItemNotFoundException(){
        String invalidItemId = "123";

        webTestClient.delete()
                .uri(BASE_URI_SHIPMENTS+"/" + VALID_SHIPMENT_ID+"/inventories/"+VALID_INVENTORY_ID+"/items/"+invalidItemId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("itemId not found: " + invalidItemId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenUpdateItemWithValidItemFields_thenReturnItem(){
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
                .uri(EXISTING_URI_ITEMS+"/"+VALID_ITEM_ID)
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
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenUpdateItemWithInvalidShipmentId_thenReturnShipmentNotFoundException(){
        String invalidShipmentId="123";

        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS+"/" + invalidShipmentId+"/inventories/"+VALID_INVENTORY_ID+"/items/"+VALID_ITEM_ID)
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
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenUpdateItemWithInvalidInventoryId_thenReturnInventoryNotFoundException(){
        String invalidInventoryId="123";

        ItemRequestModel itemRequestModel1=ItemRequestModel.builder()
                .name("La Joconde")
                .type(Type.ITEM)
                .price(BigDecimal.valueOf(100000.0))
                .description("This goes in the kitchen")
                .weight(18.0)
                .handlingInstructions("This should be handled with extreme care!")
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS+"/" + VALID_SHIPMENT_ID+"/inventories/"+invalidInventoryId+"/items/"+VALID_INVENTORY_ID)
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
    @WithMockUser(authorities = "Shipment_Owner")
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
                .uri(BASE_URI_SHIPMENTS+"/" + VALID_SHIPMENT_ID+"/inventories/"+VALID_INVENTORY_ID+"/items/"+invalidItemId)
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
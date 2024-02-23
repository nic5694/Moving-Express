package com.example.backend.shipmentsubdomain.presentationlayer.shipment;

import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.quote.ContactMethod;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.quote.QuoteStatus;
import com.example.backend.shipmentsubdomain.datalayer.shipment.Status;
import com.example.backend.shipmentsubdomain.datalayer.truck.TruckIdentifier;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.util.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql({"/data-h2.sql"})
class ShipmentControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    private final String BASE_URI_SHIPMENTS = "/api/v1/movingexpress/shipments";
    private final String VALID_SHIPMENT_ID = "c0a80121-7f5e-4d77-a5a4-5d41b04f5a57";
    private final String VALID_SHIPMENT_ID2 = "c0a80121-7f5e-4d77-a5a4-5d41b04f5a23";

    private final String VALID_OBSERVER_CODE = "MBS123";

    private final String VALID_DRIVER_ID = "auth0|65a586c28dea5e6136569cf0";

    private final String VALID_VIN = "4A3AB36F29E026270";
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
//// Assuming 'FULL' is the third value in ENUM
//        jdbcTemplate.update("INSERT INTO observers (observer_id, name, permission, observer_code, shipment_id) VALUES (?, ?, ?, ?, ?)",
//                UUID.randomUUID().toString(), "Test Observer", 2, VALID_OBSERVER_CODE, VALID_SHIPMENT_ID);

    }

    @Test
    @WithMockUser(authorities = {"Shipment_Owner"})
    public void getAllShipmentsWithUserId() {
        String userId = "someUserId";
        webTestClient.get().uri(uriBuilder -> uriBuilder.path(BASE_URI_SHIPMENTS)
                        .queryParam("userId", userId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray();
    }

    @Test
    @WithMockUser(authorities = {"Shipment_Owner"})
    public void getAllShipmentsWithEmail() {
        String email = "example@example.com";
        webTestClient.get().uri(uriBuilder -> uriBuilder.path(BASE_URI_SHIPMENTS)
                        .queryParam("email", email)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray();
    }

    @Test
    @WithMockUser(authorities = {"Shipment_Owner"})
    public void whenGetShipmentWithValidShipmentIdExists_thenReturnShipment() {
        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/" + VALID_SHIPMENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.shipmentId").isEqualTo(VALID_SHIPMENT_ID)
                .jsonPath("$.pickupAddress.streetAddress").isEqualTo("4 Oak Ave")
                .jsonPath("$.pickupAddress.city").isEqualTo("Fotville")
                .jsonPath("$.pickupAddress.country").isEqualTo("CA")
                .jsonPath("$.pickupAddress.postalCode").isEqualTo("V3V 1W1")
                .jsonPath("$.destinationAddress.streetAddress").isEqualTo("555 Pine Ave")
                .jsonPath("$.destinationAddress.city").isEqualTo("NwHoke")
                .jsonPath("$.destinationAddress.country").isEqualTo("USA")
                .jsonPath("$.destinationAddress.postalCode").isEqualTo("M4S 7H6")
                .jsonPath("$.userId").isEmpty()
                .jsonPath("$.status").isEqualTo("QUOTED")
                .jsonPath("$.shipmentName").isEqualTo("ShipmentPQR")
                .jsonPath("$.approximateWeight").isEqualTo(1500.0)
                .jsonPath("$.weight").isEqualTo(1575.0)
                .jsonPath("$.email").isEqualTo("john@icloud.com")
                .jsonPath("$.phoneNumber").isEqualTo("789-555-3123")
                .jsonPath("$.expectedMovingDate").isEqualTo("2023-12-30")
                .jsonPath("$.actualMovingDate").isEqualTo("2024-01-02");
    }

    @Test
    @WithMockUser(authorities = {"Shipment_Owner"})
    public void whenGetShipmentWithInvalidShipmentId_thenReturnShipmentNotFoundException() {
        String invalidShipmentId = "123";

        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/" + invalidShipmentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("shipmentId not found: " + invalidShipmentId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Estimator")
    public void createNewShipment_WithValidQuoteResponseModel() {
        QuoteResponseModel quoteResponseModel = QuoteResponseModel.builder()
                .pickupCountry(Country.CA)
                .pickupBuildingType("House")
                .pickupCity("CityA")
                .pickupStreetAddress("123 Main St")
                .pickupPostalCode("12345")
                .destinationCountry(Country.USA)
                .destinationBuildingType("House")
                .pickupElevator(false)
                .destinationCity("CityB")
                .destinationStreetAddress("456 Oak St")
                .destinationElevator(false)
                .destinationPostalCode("54321")
                .pickupNumberOfRooms(3)
                .destinationNumberOfRooms(4)
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john@example.com")
                .phoneNumber("")
                .expectedMovingDate(LocalDate.now())
                .contactMethod(ContactMethod.PHONE_NUMBER)
                .comment("basic comments")
                .quoteStatus(QuoteStatus.PENDING)
                .name("shipmentName")
                .approximateWeight(180000)
                .approximateShipmentValue(1010)
                .build();

        webTestClient.post()
                .uri(BASE_URI_SHIPMENTS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quoteResponseModel)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ShipmentResponseModel.class)
                .value((shipmentResponseModel1) -> {
                    assertNotNull(shipmentResponseModel1);
                    assertNotNull(shipmentResponseModel1.getShipmentId());
                    assertNotNull(shipmentResponseModel1.getPickupAddress());
                    assertNotNull(shipmentResponseModel1.getDestinationAddress());
                    assertEquals(quoteResponseModel.getPickupCountry(), shipmentResponseModel1.getPickupAddress().getCountry());
                    assertEquals(quoteResponseModel.getPickupStreetAddress(), shipmentResponseModel1.getPickupAddress().getStreetAddress());
                    assertEquals(quoteResponseModel.getPickupCity(), shipmentResponseModel1.getPickupAddress().getCity());
                    assertEquals(quoteResponseModel.getPickupPostalCode(), shipmentResponseModel1.getPickupAddress().getPostalCode());
                    assertEquals(quoteResponseModel.getDestinationCountry(), shipmentResponseModel1.getDestinationAddress().getCountry());
                    assertEquals(quoteResponseModel.getDestinationStreetAddress(), shipmentResponseModel1.getDestinationAddress().getStreetAddress());
                    assertEquals(quoteResponseModel.getDestinationCity(), shipmentResponseModel1.getDestinationAddress().getCity());
                    assertEquals(quoteResponseModel.getDestinationPostalCode(), shipmentResponseModel1.getDestinationAddress().getPostalCode());
                    assertEquals(Status.QUOTED, shipmentResponseModel1.getStatus());
                    assertEquals(quoteResponseModel.getName(), shipmentResponseModel1.getShipmentName());
                    assertEquals(quoteResponseModel.getEmailAddress(), shipmentResponseModel1.getEmail());
                    assertEquals(quoteResponseModel.getPhoneNumber(), shipmentResponseModel1.getPhoneNumber());
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void deleteShipmentWithValidShipmentId_ShouldSucceed() {
        webTestClient.delete()
                .uri(BASE_URI_SHIPMENTS + "/" + VALID_SHIPMENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .jsonPath("resultType").isEqualTo("SUCCESS");
    }

    @Test
    @WithMockUser(authorities = "Shipment_Estimator")
    public void createNewShipment_WithInvalidApproximateWeight_ShouldThrowInvalidWeightException() {
        QuoteResponseModel quoteResponseModel = QuoteResponseModel.builder()
                .pickupCountry(Country.CA)
                .pickupBuildingType("House")
                .pickupCity("CityA")
                .pickupStreetAddress("123 Main St")
                .pickupPostalCode("12345")
                .destinationCountry(Country.USA)
                .destinationBuildingType("House")
                .pickupElevator(false)
                .destinationCity("CityB")
                .destinationStreetAddress("456 Oak St")
                .destinationElevator(false)
                .destinationPostalCode("54321")
                .pickupNumberOfRooms(3)
                .destinationNumberOfRooms(4)
                .firstName("John")
                .lastName("Doe")
                .emailAddress("user@gmail.com")
                .phoneNumber("1234567890")
                .expectedMovingDate(LocalDate.now())
                .contactMethod(ContactMethod.PHONE_NUMBER)
                .comment("basic comments")
                .quoteStatus(QuoteStatus.PENDING)
                .name("shipmentName")
                .approximateWeight(-4.0)
                .approximateShipmentValue(10000)
                .build();

        webTestClient.post()
                .uri(BASE_URI_SHIPMENTS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quoteResponseModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid approximateWeight: " + quoteResponseModel.getApproximateWeight());
    }

    @Test
    @WithMockUser(authorities = "Shipment_Estimator")
    public void createNewShipment_WithInvalidApproximateShipmentValue_ShouldThrowInvalidApproximateShipmentValue() {
        QuoteResponseModel quoteResponseModel = QuoteResponseModel.builder()
                .pickupCountry(Country.CA)
                .pickupBuildingType("House")
                .pickupCity("CityA")
                .pickupStreetAddress("123 Main St")
                .pickupPostalCode("12345")
                .destinationCountry(Country.USA)
                .destinationBuildingType("House")
                .pickupElevator(false)
                .destinationCity("CityB")
                .destinationStreetAddress("456 Oak St")
                .destinationElevator(false)
                .destinationPostalCode("54321")
                .pickupNumberOfRooms(3)
                .destinationNumberOfRooms(4)
                .firstName("John")
                .lastName("Doe")
                .emailAddress("user@gmail.com")
                .phoneNumber("1234567890")
                .expectedMovingDate(LocalDate.now())
                .contactMethod(ContactMethod.PHONE_NUMBER)
                .comment("basic comments")
                .quoteStatus(QuoteStatus.PENDING)
                .name("shipmentName")
                .approximateWeight(180000)
                .approximateShipmentValue(-1010)
                .build();

        webTestClient.post()
                .uri(BASE_URI_SHIPMENTS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quoteResponseModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid shipmentValue: " + quoteResponseModel.getApproximateShipmentValue());
    }

    @Test
    public void getShipmentDetailsByObserverCode_ValidObserverCode_ShouldReturnShipmentDetails() {
        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/observer/" + VALID_OBSERVER_CODE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.shipmentId").isEqualTo(VALID_SHIPMENT_ID2)
                .jsonPath("$.pickupAddress").exists()
                .jsonPath("$.destinationAddress").exists();
    }

    @Test
    public void getShipmentDetailsByObserverCode_InvalidObserverCode_ShouldReturnNotFoundError() {
        String invalidObserverCode = "INVALIDOBSERVERCODE";

        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/observer/" + invalidObserverCode)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Observer with code " + invalidObserverCode + " not found");
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void getShipmentsByDriverId_ValidDriverId_ShouldReturnShipments() throws Exception {
        String validDriverId = "auth0|65a586c28dea5e6136569cf0";

        mockMvc.perform(get(BASE_URI_SHIPMENTS + "/assigned/driver")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject(validDriverId)).authorities(new SimpleGrantedAuthority("Truck_Driver")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void getShipmentsByDriverId_InvalidDriverId_ShouldReturnUserNotFoundException() throws Exception {
        String invalidDriverId = "auth0|123";
        mockMvc.perform(get(BASE_URI_SHIPMENTS + "/assigned/driver")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject(invalidDriverId)).authorities(new SimpleGrantedAuthority("Truck_Driver")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with userId: " + invalidDriverId + " could not be found."));
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void getShipmentDetails_ValidShipmentId_ShouldReturnShipmentDetails() {
        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/" + VALID_SHIPMENT_ID + "/details")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ShipmentObserversInventoriesItemsResponseModel.class)
                .value(shipmentObserversInventoriesItemsResponseModel -> {
                    assertNotNull(shipmentObserversInventoriesItemsResponseModel);
                    assertEquals(VALID_SHIPMENT_ID, shipmentObserversInventoriesItemsResponseModel.getShipmentId());
                    assertNotNull(shipmentObserversInventoriesItemsResponseModel.getPickupAddress());
                    assertNotNull(shipmentObserversInventoriesItemsResponseModel.getDestinationAddress());
                    assertNotNull(shipmentObserversInventoriesItemsResponseModel.getObservers());
                    assertNotNull(shipmentObserversInventoriesItemsResponseModel.getInventoriesItemsResponseModels());
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void getShipmentDetails_InvalidShipmentId_ShouldReturnShipmentNotFoundException() {
        String invalidShipmentId = "INVALIDSHIPMENTID";
        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/" + invalidShipmentId + "/details")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Shipment not found for shipmentId: " + invalidShipmentId);
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void getShipmentDetailsForDriverReport_ValidShipmentId_ShouldReturnShipmentDetails() {
        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/" + VALID_SHIPMENT_ID + "/driver-report")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ShipmentInventoriesItemsResponseModel.class)
                .value(shipmentInventoriesItemsResponseModel -> {
                    assertNotNull(shipmentInventoriesItemsResponseModel);
                    assertEquals(VALID_SHIPMENT_ID, shipmentInventoriesItemsResponseModel.getShipmentId());
                    assertNotNull(shipmentInventoriesItemsResponseModel.getPickupAddress());
                    assertNotNull(shipmentInventoriesItemsResponseModel.getDestinationAddress());
                    assertNotNull(shipmentInventoriesItemsResponseModel.getInventoriesItemsResponseModels());
                });
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void getShipmentDetailsForDriverReport_InvalidShipmentId_ShouldReturnShipmentNotFoundException() {
        String invalidShipmentId = "INVALIDSHIPMENTID";
        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/" + invalidShipmentId + "/driver-report")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Shipment not found for shipmentId: " + invalidShipmentId);
    }

    private Address convertToAddress(AddressResponseModel addressResponseModel) {
        return Address.builder()
                .streetAddress(addressResponseModel.getStreetAddress())
                .city(addressResponseModel.getCity())
                .postalCode(addressResponseModel.getPostalCode())
                .country(addressResponseModel.getCountry())
                .build();
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void getUnassignedShipments_ShouldReturnShipments() {
        webTestClient.get()
                .uri(BASE_URI_SHIPMENTS + "/unassigned/driver")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ShipmentResponseModel.class)
                .value(result -> {
                    assertNotNull(result);
                    assertEquals(2, result.size());

                    assertEquals("c0a80121-7f5e-4d77-a5a4-5d41b04f5a24", result.get(0).getShipmentId());
                    assertEquals(Status.QUOTED, result.get(0).getStatus());
                    assertEquals(LocalDate.parse("2023-12-31"), result.get(0).getExpectedMovingDate());
                    assertEquals(LocalDate.parse("2024-01-03"), result.get(0).getActualMovingDate());
                    assertEquals("ShipmentXYZ", result.get(0).getShipmentName());
                    assertEquals(1750.0, result.get(0).getApproximateWeight());
                    assertEquals(1837.5, result.get(0).getWeight());
                    assertEquals("john@example.com", result.get(0).getEmail());
                    assertEquals("789-555-3123", result.get(0).getPhoneNumber());
                    assertEquals(300.0, result.get(0).getApproximateShipmentValue());

                    assertEquals("c0a80121-7f5e-4d77-a5a4-5d41b04f5a25", result.get(1).getShipmentId());
                    assertEquals(Status.QUOTED, result.get(1).getStatus());
                    assertEquals(LocalDate.parse("2024-01-01"), result.get(1).getExpectedMovingDate());
                    assertEquals(LocalDate.parse("2024-01-04"), result.get(1).getActualMovingDate());
                    assertEquals("ShipmentABC", result.get(1).getShipmentName());
                    assertEquals(2000.0, result.get(1).getApproximateWeight());
                    assertEquals(2100.0, result.get(1).getWeight());
                    assertEquals("john@example.com", result.get(1).getEmail());
                    assertEquals("789-555-3123", result.get(1).getPhoneNumber());
                    assertEquals(350.0, result.get(1).getApproximateShipmentValue());
                });
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void assignDriverAndTruckToShipment_ShouldReturnShipmentNotFound() {
        String invalidShipmentId = "invalid_shipment_Id";

        TruckIdentifier truckIdentifier = new TruckIdentifier(VALID_VIN);

        AssignDriverAndTruckToShipmentRequest assignDriverAndTruckToShipmentRequest = AssignDriverAndTruckToShipmentRequest.builder()
                .shipmentId(invalidShipmentId)
                .driverId(VALID_DRIVER_ID)
                .vin(truckIdentifier)
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(assignDriverAndTruckToShipmentRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Shipment with id " + invalidShipmentId + " was not found.");

    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void assignDriverAndTruckToShipment_ShouldSucceed() {

        TruckIdentifier truckIdentifier = new TruckIdentifier(VALID_VIN);

        AssignDriverAndTruckToShipmentRequest assignDriverAndTruckToShipmentRequest = AssignDriverAndTruckToShipmentRequest.builder()
                .shipmentId(VALID_SHIPMENT_ID)
                .driverId(VALID_DRIVER_ID)
                .vin(truckIdentifier)
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(assignDriverAndTruckToShipmentRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ShipmentResponseModel.class)
                .value(shipmentResponseModel -> {
                    assertNotNull(shipmentResponseModel);
                    assertNotNull(shipmentResponseModel.getShipmentId());
                    assertEquals(shipmentResponseModel.getShipmentId(), assignDriverAndTruckToShipmentRequest.getShipmentId());
                    assertEquals(shipmentResponseModel.getDriverId(), assignDriverAndTruckToShipmentRequest.getDriverId());
                    assertEquals(shipmentResponseModel.getTruckId(), assignDriverAndTruckToShipmentRequest.getVin().getVin());
                });
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void unassignShipment_WithValidDriverId_ShouldSucceed() throws Exception {
        String validDriverId = "auth0|65a586c28dea5e6136569cf0";

        AddressResponseModel pickupAddressResponse = AddressResponseModel.builder()
                .addressId("a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5p1")
                .city("Fotville")
                .streetAddress("4 Oak Ave")
                .country(Country.CA)
                .postalCode("V3V 1W1")
                .build();

        AddressResponseModel destinationAddressResponse = AddressResponseModel.builder()
                .addressId("a1sc3d4-e5f6-g7h8-i9j0-oil2m3n4o5p2")
                .city("NwHoke")
                .streetAddress("555 Pine Ave")
                .country(Country.USA)
                .postalCode("M4S 7H6")
                .build();

        ShipmentResponseModel shipmentResponseModel = ShipmentResponseModel.builder()
                .shipmentId("c0a80121-7f5e-4d77-a5a4-5d41b04f5a57")
                .status(Status.QUOTED)
                .expectedMovingDate(LocalDate.of(2023, 12, 30))
                .actualMovingDate(LocalDate.of(2024, 1, 2))
                .shipmentName("ShipmentPQR")
                .approximateWeight(1500.0)
                .weight(1575.0)
                .pickupAddress(pickupAddressResponse)
                .destinationAddress(destinationAddressResponse)
                .userId(null)
                .truckId("f4b29e3c-a526-487d-84ec-7c16791b26b6")
                .email("john@icloud.com")
                .phoneNumber("789-555-3123")
                .approximateShipmentValue(250.0)
                .driverId("auth0|65a586c28dea5e6136569cf0")
                .build();

        mockMvc.perform(put(BASE_URI_SHIPMENTS+"/"+VALID_SHIPMENT_ID+"/driver")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject(validDriverId)).authorities(new SimpleGrantedAuthority("Truck_Driver")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentId").exists()) // Add your assertions here
                .andExpect(jsonPath("$.pickupAddress").exists())
                .andExpect(jsonPath("$.destinationAddress").exists())
                .andExpect(jsonPath("$.pickupAddress.addressId").value(shipmentResponseModel.getPickupAddress().getAddressId()))
                .andExpect(jsonPath("$.pickupAddress.streetAddress").value(shipmentResponseModel.getPickupAddress().getStreetAddress()))
                .andExpect(jsonPath("$.pickupAddress.city").value(shipmentResponseModel.getPickupAddress().getCity()))
                .andExpect(jsonPath("$.pickupAddress.country").value(shipmentResponseModel.getPickupAddress().getCountry().toString()))
                .andExpect(jsonPath("$.pickupAddress.postalCode").value(shipmentResponseModel.getPickupAddress().getPostalCode()))
                .andExpect(jsonPath("$.destinationAddress.addressId").value(shipmentResponseModel.getDestinationAddress().getAddressId()))
                .andExpect(jsonPath("$.destinationAddress.streetAddress").value(shipmentResponseModel.getDestinationAddress().getStreetAddress()))
                .andExpect(jsonPath("$.destinationAddress.city").value(shipmentResponseModel.getDestinationAddress().getCity()))
                .andExpect(jsonPath("$.destinationAddress.country").value(shipmentResponseModel.getDestinationAddress().getCountry().toString()))
                .andExpect(jsonPath("$.destinationAddress.postalCode").value(shipmentResponseModel.getDestinationAddress().getPostalCode()))
                .andExpect(jsonPath("$.status").value(Status.QUOTED.toString()))
                .andExpect(jsonPath("$.shipmentName").value(shipmentResponseModel.getShipmentName()))
                .andExpect(jsonPath("$.email").value(shipmentResponseModel.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(shipmentResponseModel.getPhoneNumber()))
                .andExpect(jsonPath("$.driverId").doesNotExist())
                .andExpect(jsonPath("$.truckId").doesNotExist());
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void assignFinalWeightToShipment_ShouldReturnShipmentNotFound() {
        String invalidShipmentId = "invalid_shipment_Id";

        double finalWeight = 5000;

        FinalWeightRequestModel finalWeightRequestModel = FinalWeightRequestModel.builder()
                .shipmentId(invalidShipmentId)
                .finalWeight(finalWeight)
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS + "/setShipmentFinalWeight")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(finalWeightRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Shipment with id " + invalidShipmentId + " was not found.");
    }
    
    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void unassignShipment_WithInvalidShipmentId_ShouldThrowShipmentNotFoundException() throws Exception {
        String validDriverId = "auth0|65a586c28dea5e6136569cf0";
        String invalidShipmentId = "123";

        mockMvc.perform(put(BASE_URI_SHIPMENTS+"/"+invalidShipmentId+"/driver")
                .with(csrf())
                .with(oidcLogin().idToken(i -> i.subject(validDriverId)).authorities(new SimpleGrantedAuthority("Truck_Driver")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("shipmentId not found: "+invalidShipmentId));
    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void assignFinalWeightToShipment_ShouldSucceed() {

        double finalWeight = 5000;

        FinalWeightRequestModel finalWeightRequestModel = FinalWeightRequestModel.builder()
                .shipmentId(VALID_SHIPMENT_ID)
                .finalWeight(finalWeight)
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS + "/setShipmentFinalWeight")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(finalWeightRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ShipmentResponseModel.class)
                .value(shipmentResponseModel -> {
                    assertNotNull(shipmentResponseModel);
                    assertNotNull(shipmentResponseModel.getShipmentId());
                    assertEquals(shipmentResponseModel.getShipmentId(), finalWeightRequestModel.getShipmentId());
                    assertEquals(shipmentResponseModel.getWeight(), finalWeightRequestModel.getFinalWeight());
                });


    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void updateShipmentStatus_ShouldReturnShipmentNotFound() {
        String invalidShipmentId = "invalid_shipment_Id";

        ShipmentRequestModel shipmentRequestModel = ShipmentRequestModel.builder()
                .pickupAddressId("pickupAddressId")
                .destinationAddressId("destinationAddressId")
                .clientId("clientId")
                .truckId("truckId")
                .status(Status.LOADING)
                .approximateWeight(5000)
                .approximateShipmentValue(1000)
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS + "/" + invalidShipmentId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(shipmentRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Shipment with id " + invalidShipmentId + " was not found.");

    }

    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void updateShipmentStatus_ShouldSucceed() {

        ShipmentRequestModel shipmentRequestModel = ShipmentRequestModel.builder()
                .pickupAddressId("pickupAddressId")
                .destinationAddressId("destinationAddressId")
                .clientId("clientId")
                .truckId("truckId")
                .status(Status.LOADING)
                .approximateWeight(5000)
                .approximateShipmentValue(1000)
                .build();

        webTestClient.put()
                .uri(BASE_URI_SHIPMENTS + "/" + VALID_SHIPMENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(shipmentRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ShipmentResponseModel.class)
                .value(shipmentResponseModel -> {
                    assertNotNull(shipmentResponseModel);
                    assertNotNull(shipmentResponseModel.getShipmentId());
                    assertEquals(shipmentResponseModel.getShipmentId(), VALID_SHIPMENT_ID);
                    assertEquals(shipmentResponseModel.getStatus(), shipmentRequestModel.getStatus());
                });
    }
    
    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void unassignShipment_WithInvalidDriverId_ShouldThrowUserNotFoundException() throws Exception {
        String invalidDriverId = "auth0|123";

        mockMvc.perform(put(BASE_URI_SHIPMENTS+"/"+VALID_SHIPMENT_ID+"/driver")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject(invalidDriverId)).authorities(new SimpleGrantedAuthority("Truck_Driver")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with userId: " + invalidDriverId + " could not be found."));
    }
}
package com.example.backend.shipmentsubdomain.presentationlayer.quote;

import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.quote.*;
import com.example.backend.shipmentsubdomain.presentationlayer.event.EventRequestModel;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Sql({"/data-h2.sql"})
class QuoteControllerIntegrationTest {
    private final String BASE_URI_QUOTES = "/api/v1/movingexpress/quotes";
    private final String BASE_URI_QUOTES_REQUEST = "/api/v1/movingexpress/quotes/request";
    private final String VALID_QUOTE_ID = "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    QuoteRepository quoteRepository;

    Quote existingQuote;

    @BeforeEach
    public void setUp() {

        PickupAddress pickupAddress = new PickupAddress("123 Main St",
                "CityA",
                Country.CA,
                "12345",
                4,
                true,
                "Apartment");

        DestinationAddress destinationAddress = new DestinationAddress("456 Oak St",
                "CityB",
                Country.USA,
                "54321",
                4,
                false,
                "House");

        ContactDetails contactDetails = new ContactDetails("John",
                "Doe", "Example.doe@example.com", "123-456-7890");

        this.existingQuote = new Quote();

        existingQuote.setPickupAddress(pickupAddress);
        existingQuote.setDestinationAddress(destinationAddress);
        existingQuote.setContactDetails(contactDetails);
        existingQuote.setExpectedMovingDate(LocalDate.of(2023, 1, 1));
        existingQuote.setContactMethod(ContactMethod.EMAIL);
        existingQuote.setQuoteStatus(QuoteStatus.PENDING);
        existingQuote.setComment("Additional comments go here");
        existingQuote.setApproximateWeight(0.0);
        existingQuote.setApproximateShipmentValue(0.0);

        quoteRepository.save(existingQuote);
    }

    @AfterEach
    public void tearDown() {
        quoteRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void WhenGetAllQuotesByValidEmail_ThenReturnMatchingQuotes(){

        int expectedSize = 1;

        String testEmail = "emilyd@example.com";
        webTestClient.get()
                .uri(BASE_URI_QUOTES + "/Email/" + testEmail)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(QuoteResponseModel.class)
                .consumeWith(response -> {

                    List<QuoteResponseModel> quoteResponseModels = response.getResponseBody();

                    assertThat(quoteResponseModels).isNotEmpty();

                    assertEquals(quoteResponseModels.size(), expectedSize);
                    assertEquals(quoteResponseModels.get(0).getQuoteId(), "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6");
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void WhenGetAllQuotesByStatusPending_ThenReturnPendingQuotes() {
        String URL_PENDING_QUOTES = "/api/v1/movingexpress/quotes?quoteStatus=PENDING";

        int expectedSize = 3;
        webTestClient.get()
                .uri(URL_PENDING_QUOTES)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(QuoteResponseModel.class)
                .consumeWith(response -> {

                    List<QuoteResponseModel> quoteResponseModels = response.getResponseBody();

                    assertThat(quoteResponseModels).isNotEmpty();

                    assertEquals(quoteResponseModels.size(), expectedSize);
                });
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetQuoteWithValidQuoteIdExists_thenReturnQuote(){
        webTestClient.get()
                .uri(BASE_URI_QUOTES + "/" + VALID_QUOTE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.quoteId").isEqualTo("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6")
                .jsonPath("$.expectedMovingDate").isEqualTo("2023-12-15")
                .jsonPath("$.contactMethod").isEqualTo("EMAIL")
                .jsonPath("$.comment").isEqualTo("Moving details for John Doe")

                .jsonPath("$.pickupStreetAddress").isEqualTo("123 Main St")
                .jsonPath("$.pickupCity").isEqualTo("Cityville")
                .jsonPath("$.pickupCountry").isEqualTo("CA")
                .jsonPath("$.pickupPostalCode").isEqualTo("M1A 1A1")
                .jsonPath("$.pickupNumberOfRooms").isEqualTo(3)
                .jsonPath("$.pickupElevator").isEqualTo(true)
                .jsonPath("$.pickupBuildingType").isEqualTo("Apartment")

                .jsonPath("$.destinationStreetAddress").isEqualTo("456 Oak St")
                .jsonPath("$.destinationCity").isEqualTo("Townsville")
                .jsonPath("$.destinationCountry").isEqualTo("USA")
                .jsonPath("$.destinationPostalCode").isEqualTo("M5V 2H1")
                .jsonPath("$.destinationNumberOfRooms").isEqualTo(4)
                .jsonPath("$.destinationElevator").isEqualTo(false)
                .jsonPath("$.destinationBuildingType").isEqualTo("House")

                .jsonPath("$.firstName").isEqualTo("John")
                .jsonPath("$.lastName").isEqualTo("Doe")
                .jsonPath("$.emailAddress").isEqualTo("emilyd@example.com")
                .jsonPath("$.phoneNumber").isEqualTo("123-555-1234");
    }

    @Test
    @WithMockUser(authorities = "Shipment_Owner")
    public void whenGetQuoteWithInvalidQuoteId_thenReturnNotFoundException(){
        String invalidQuoteId = "123";

        webTestClient.get()
                .uri(BASE_URI_QUOTES + "/" + invalidQuoteId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("quoteId not found: " + invalidQuoteId);
    }

    @Test
    public void whenAddQuoteWithValidValues_thenReturnNewQuote() {
        // arrange
        QuoteRequestModel quoteRequestModel = QuoteRequestModel.builder()
                .pickupStreetAddress("123 Main St")
                .pickupCity("CityA")
                .pickupCountry(Country.CA)
                .pickupPostalCode("12345")
                .pickupNumberOfRooms(4)
                .pickupElevator(true)
                .pickupBuildingType("Apartment")
                .destinationStreetAddress("456 Oak St")
                .destinationCity("CityB")
                .destinationCountry(Country.USA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(4)
                .destinationElevator(false)
                .destinationBuildingType("House")
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john.doe@example.com")
                .phoneNumber("123-456-7890")
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .contactMethod(ContactMethod.EMAIL)
                .comment("Additional comments go here")
                .shipmentName("This is John's shipment.")
                .approximateWeight(1500.0)
                .approximateShipmentValue(10000.0)
                .build();

        // act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quoteRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.quoteId").isNotEmpty()
                .jsonPath("$.pickupStreetAddress").isEqualTo("123 Main St")
                .jsonPath("$.pickupCity").isEqualTo("CityA")
                .jsonPath("$.pickupCountry").isEqualTo("CA")
                .jsonPath("$.pickupPostalCode").isEqualTo("12345")
                .jsonPath("$.pickupNumberOfRooms").isEqualTo(4)
                .jsonPath("$.pickupElevator").isEqualTo(true)
                .jsonPath("$.pickupBuildingType").isEqualTo("Apartment")
                .jsonPath("$.destinationStreetAddress").isEqualTo("456 Oak St")
                .jsonPath("$.destinationCity").isEqualTo("CityB")
                .jsonPath("$.destinationCountry").isEqualTo("USA")
                .jsonPath("$.destinationPostalCode").isEqualTo("54321")
                .jsonPath("$.destinationNumberOfRooms").isEqualTo(4)
                .jsonPath("$.destinationElevator").isEqualTo(false)
                .jsonPath("$.destinationBuildingType").isEqualTo("House")
                .jsonPath("$.firstName").isEqualTo("John")
                .jsonPath("$.lastName").isEqualTo("Doe")
                .jsonPath("$.emailAddress").isEqualTo("john.doe@example.com")
                .jsonPath("$.phoneNumber").isEqualTo("123-456-7890")
                .jsonPath("$.contactMethod").isEqualTo("EMAIL")
                .jsonPath("$.expectedMovingDate").isEqualTo("2023-01-01")
                .jsonPath("$.comment").isEqualTo("Additional comments go here");
    }

    @Test
    @WithMockUser(authorities = "Shipment_Reviewer")
    public void whenCreateQuoteEventWithValidValues_decline_thenReturnNewEvent() {
        //arrange
        String quoteId = existingQuote.getQuoteIdentifier().getQuoteId();


        EventRequestModel eventRequestModel = EventRequestModel.builder()
                .event("decline")
                .build();

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES + "/" + quoteId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();


        assertEquals(QuoteStatus.DECLINED,
                quoteRepository.findByQuoteIdentifier_QuoteId(quoteId).getQuoteStatus());
    }

    @Test
    @WithMockUser(authorities = "Shipment_Reviewer")
    public void whenCreateQuoteEventWithValidValues_accept_thenReturnNewEvent() {
        //arrange
        String quoteId = existingQuote.getQuoteIdentifier().getQuoteId();

        EventRequestModel eventRequestModel = EventRequestModel.builder()
                .event("accept")
                .build();

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES + "/" + quoteId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();


        assertEquals(QuoteStatus.ACCEPTED,
                quoteRepository.findByQuoteIdentifier_QuoteId(quoteId).getQuoteStatus());
    }

    @Test
    @WithMockUser(authorities = "Shipment_Reviewer")
    public void whenCreateQuoteEventWithValidValues_convertQuoteToShipment_thenReturnNewEvent() {
        //arrange
        String quoteId = existingQuote.getQuoteIdentifier().getQuoteId();

        EventRequestModel eventRequestModel = EventRequestModel.builder()
                .event("convert")
                .build();

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES + "/" + quoteId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();


        assertEquals(QuoteStatus.CREATED,
                quoteRepository.findByQuoteIdentifier_QuoteId(quoteId).getQuoteStatus());
    }

    @Test
    @WithMockUser(authorities = "Shipment_Reviewer")
    public void whenCreateQuoteEventWithInvalidValues_thenThrowException() {
        // arrange
        String quoteId = existingQuote.getQuoteIdentifier().getQuoteId();

        EventRequestModel eventRequestModel = EventRequestModel.builder()
                .event("NotGOOD")
                .build();

        // act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES + "/" + quoteId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Unexpected event value: NotGOOD");
    }

    @Test
    public void whenAddQuoteWithInvalidPickupNumberOfRooms_ThenThrowInvalidNumberOfRoomsException(){
        // arrange
        QuoteRequestModel quoteRequestModel = QuoteRequestModel.builder()
                .pickupStreetAddress("123 Main St")
                .pickupCity("CityA")
                .pickupCountry(Country.CA)
                .pickupPostalCode("12345")
                .pickupNumberOfRooms(-4)
                .pickupElevator(true)
                .pickupBuildingType("Apartment")
                .destinationStreetAddress("456 Oak St")
                .destinationCity("CityB")
                .destinationCountry(Country.USA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(4)
                .destinationElevator(false)
                .destinationBuildingType("House")
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john.doe@example.com")
                .phoneNumber("123-456-7890")
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .contactMethod(ContactMethod.EMAIL)
                .comment("Additional comments go here")
                .shipmentName("This is John's shipment.")
                .approximateWeight(1500.0)
                .approximateShipmentValue(10000.0)
                .build();

        // act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quoteRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid pickupNumberOfRooms: " + quoteRequestModel.getPickupNumberOfRooms());
    }

    @Test
    public void whenAddQuoteWithInvalidDestinationNumberOfRooms_ThenThrowInvalidNumberOfRoomsException(){
        // arrange
        QuoteRequestModel quoteRequestModel = QuoteRequestModel.builder()
                .pickupStreetAddress("123 Main St")
                .pickupCity("CityA")
                .pickupCountry(Country.CA)
                .pickupPostalCode("12345")
                .pickupNumberOfRooms(4)
                .pickupElevator(true)
                .pickupBuildingType("Apartment")
                .destinationStreetAddress("456 Oak St")
                .destinationCity("CityB")
                .destinationCountry(Country.USA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(-4)
                .destinationElevator(false)
                .destinationBuildingType("House")
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john.doe@example.com")
                .phoneNumber("123-456-7890")
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .contactMethod(ContactMethod.EMAIL)
                .comment("Additional comments go here")
                .shipmentName("This is John's shipment.")
                .approximateWeight(1500.0)
                .approximateShipmentValue(10000.0)
                .build();

        // act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quoteRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid destinationNumberOfRooms: " + quoteRequestModel.getDestinationNumberOfRooms());
    }

    @Test
    @WithMockUser(authorities = "Shipment_Reviewer")
    public void whenCreateQuoteEventWithInvalidShipmentId_decline_thenReturnQuoteNotFoundException() {
        //arrange
        String invalidQuoteId = "123";


        EventRequestModel eventRequestModel = EventRequestModel.builder()
                .event("decline")
                .build();

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES + "/" + invalidQuoteId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("quoteId not found: " + invalidQuoteId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Reviewer")
    public void whenCreateQuoteEventWithInvalidShipmentId_accept_thenReturnQuoteNotFoundException() {
        //arrange
        String invalidQuoteId = "123";

        EventRequestModel eventRequestModel = EventRequestModel.builder()
                .event("accept")
                .build();

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES + "/" + invalidQuoteId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("quoteId not found: " + invalidQuoteId);
    }

    @Test
    @WithMockUser(authorities = "Shipment_Reviewer")
    public void whenCreateQuoteEventWithInvalidShipmentId_convertQuoteToShipment_thenReturnQuoteNotFoundException() {
        //arrange
        String invalidQuoteId = "123";

        EventRequestModel eventRequestModel = EventRequestModel.builder()
                .event("convert")
                .build();

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES + "/" + invalidQuoteId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("quoteId not found: " + invalidQuoteId);
    }

    @Test
    public void whenAddQuoteWithSamePickupAndDestinationAddress_ThenThrowInvalidAddressException(){
        // arrange
        QuoteRequestModel quoteRequestModel = QuoteRequestModel.builder()
                .pickupStreetAddress("123 Main St")
                .pickupCity("CityA")
                .pickupCountry(Country.CA)
                .pickupPostalCode("12345")
                .pickupNumberOfRooms(4)
                .pickupElevator(true)
                .pickupBuildingType("Apartment")
                .destinationStreetAddress("123 Main St")
                .destinationCity("CityA")
                .destinationCountry(Country.CA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(4)
                .destinationElevator(false)
                .destinationBuildingType("House")
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john.doe@example.com")
                .phoneNumber("123-456-7890")
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .contactMethod(ContactMethod.EMAIL)
                .comment("Additional comments go here")
                .shipmentName("This is John's shipment.")
                .approximateWeight(1500.0)
                .approximateShipmentValue(10000.0)
                .build();

        String pickupLocation=quoteRequestModel.getPickupStreetAddress()+", "+quoteRequestModel.getPickupCity()+", "+quoteRequestModel.getPickupCountry();

        // act and assert
        webTestClient.post()
                .uri(BASE_URI_QUOTES_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quoteRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("invalid pickup and destination address: " + pickupLocation);
    }
}
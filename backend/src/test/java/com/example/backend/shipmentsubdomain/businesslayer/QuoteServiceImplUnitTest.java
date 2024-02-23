package com.example.backend.shipmentsubdomain.businesslayer;

import com.example.backend.shipmentsubdomain.businesslayer.quote.QuoteServiceImpl;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.quote.*;
import com.example.backend.shipmentsubdomain.datamapperlayer.quote.QuoteRequestMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.quote.QuoteResponseMapper;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteRequestModel;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.util.EmailUtil;
import com.example.backend.util.exceptions.QuoteNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
class QuoteServiceImplUnitTest {

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private QuoteRequestMapper quoteRequestMapper;

    @Mock
    private QuoteResponseMapper quoteResponseMapper;

    @InjectMocks
    private QuoteServiceImpl quoteService;

    private QuoteRequestModel sampleQuoteRequestModel;
    private Quote sampleQuote;
    private QuoteResponseModel sampleQuoteResponseModel;
    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private EmailUtil emailUtil;

    @BeforeEach
    void setUp() {
        sampleQuoteRequestModel = QuoteRequestModel.builder()
                .pickupStreetAddress("123 Main St")
                .pickupCity("Anytown")
                .pickupCountry(Country.USA)
                .pickupPostalCode("12345")
                .pickupNumberOfRooms(5)
                .pickupElevator(true)
                .pickupBuildingType("Apartment")
                .destinationStreetAddress("456 Market St")
                .destinationCity("Othertown")
                .destinationCountry(Country.CA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(4)
                .destinationElevator(false)
                .destinationBuildingType("House")
                .firstName("John")
                .lastName("Doe")
                .emailAddress("johndoe@example.com")
                .phoneNumber("555-1234")
                .contactMethod(ContactMethod.EMAIL)
                .expectedMovingDate(LocalDate.now())
                .comment("Moving soon")
                .shipmentName("This is John's shipment.")
                .approximateWeight(1740.0)
                .approximateShipmentValue(15000.0)
                .build();

        sampleQuote = buildQuote();
        sampleQuoteResponseModel = buildQuoteResponse();
    }

    @Test
    void getQuoteWithValidQuoteId_ShouldSucceed(){
        //Arrange
        String quoteId="7d08a2a7-368c-49e8-9f1d-42f1b0196bc5";

        sampleQuote = buildQuote();

        sampleQuoteResponseModel = buildQuoteResponse();

        Mockito.when(quoteResponseMapper.entityToResponseModel(sampleQuote)).thenReturn(sampleQuoteResponseModel);
        Mockito.when(quoteRepository.findByQuoteIdentifier_QuoteId(quoteId)).thenReturn(sampleQuote);

        //Act
        QuoteResponseModel quoteResponse=quoteService.getQuote(quoteId);

        assertThat(quoteResponse.getExpectedMovingDate()).isEqualTo(sampleQuote.getExpectedMovingDate());
        assertThat(quoteResponse.getContactMethod()).isEqualTo(sampleQuote.getContactMethod());
        assertThat(quoteResponse.getComment()).isEqualTo(sampleQuote.getComment());

        assertThat(quoteResponse.getPickupStreetAddress()).isEqualTo(sampleQuote.getPickupAddress().getPickupStreetAddress());
        assertThat(quoteResponse.getPickupCity()).isEqualTo(sampleQuote.getPickupAddress().getPickupCity());
        assertThat(quoteResponse.getPickupCountry()).isEqualTo(sampleQuote.getPickupAddress().getPickupCountry());
        assertThat(quoteResponse.getPickupPostalCode()).isEqualTo(sampleQuote.getPickupAddress().getPickupPostalCode());
        assertThat(quoteResponse.getPickupNumberOfRooms()).isEqualTo(sampleQuote.getPickupAddress().getPickupNumberOfRooms());
        assertThat(quoteResponse.isPickupElevator()).isEqualTo(sampleQuote.getPickupAddress().isPickupElevator());
        assertThat(quoteResponse.getPickupBuildingType()).isEqualTo(sampleQuote.getPickupAddress().getPickupBuildingType());

        assertThat(quoteResponse.getDestinationStreetAddress()).isEqualTo(sampleQuote.getDestinationAddress().getDestinationStreetAddress());
        assertThat(quoteResponse.getDestinationCity()).isEqualTo(sampleQuote.getDestinationAddress().getDestinationCity());
        assertThat(quoteResponse.getDestinationCountry()).isEqualTo(sampleQuote.getDestinationAddress().getDestinationCountry());
        assertThat(quoteResponse.getDestinationPostalCode()).isEqualTo(sampleQuote.getDestinationAddress().getDestinationPostalCode());
        assertThat(quoteResponse.getDestinationNumberOfRooms()).isEqualTo(sampleQuote.getDestinationAddress().getDestinationNumberOfRooms());
        assertThat(quoteResponse.isDestinationElevator()).isEqualTo(sampleQuote.getDestinationAddress().isDestinationElevator());
        assertThat(quoteResponse.getDestinationBuildingType()).isEqualTo(sampleQuote.getDestinationAddress().getDestinationBuildingType());

        assertThat(quoteResponse.getFirstName()).isEqualTo(sampleQuote.getContactDetails().getFirstName());
        assertThat(quoteResponse.getLastName()).isEqualTo(sampleQuote.getContactDetails().getLastName());
        assertThat(quoteResponse.getEmailAddress()).isEqualTo(sampleQuote.getContactDetails().getEmailAddress());
        assertThat(quoteResponse.getPhoneNumber()).isEqualTo(sampleQuote.getContactDetails().getPhoneNumber());

        Mockito.verify(quoteRepository, Mockito.times(1)).findByQuoteIdentifier_QuoteId(quoteId);
        Mockito.verify(quoteResponseMapper, Mockito.times(1)).entityToResponseModel(sampleQuote);
    }

    @Test
    void getQuoteWithInvalidQuoteId_ShouldNotSucceed(){
        //Act
        String quoteId="123";

        sampleQuote = buildQuote();

        sampleQuoteResponseModel = buildQuoteResponse();

        Mockito.when(quoteRepository.findByQuoteIdentifier_QuoteId(quoteId)).thenThrow(new QuoteNotFoundException("quoteId not found: "+ quoteId));

        try {
            QuoteResponseModel quoteResponse = quoteService.getQuote(quoteId);

            fail("Expected QuoteNotFoundException, but got QuoteResponse: " + quoteResponse);
        } catch (QuoteNotFoundException e) {
            assertThat(e.getMessage()).contains("quoteId not found: " + quoteId);
        }
    }

    @AfterEach
    void tearDown(){
        quoteRepository.deleteAll();
    }

    @Test
    void addQuote_ShouldSucceed() {
        // Arrange
        when(quoteRequestMapper.requestModelToEntity(any(QuoteRequestModel.class))).thenReturn(sampleQuote);
        when(quoteRepository.save(any(Quote.class))).thenReturn(sampleQuote);
        when(quoteResponseMapper.entityToResponseModel(any(Quote.class))).thenReturn(sampleQuoteResponseModel);

        // Act
        QuoteResponseModel result = quoteService.addQuote(sampleQuoteRequestModel);

        // Assert
        assertEquals(sampleQuoteResponseModel, result);
        verify(quoteRequestMapper, Mockito.times(1)).requestModelToEntity(any(QuoteRequestModel.class));
        verify(quoteRepository, Mockito.times(1)).save(any(Quote.class));
        verify(quoteResponseMapper, Mockito.times(1)).entityToResponseModel(any(Quote.class));
    }

    @Test
    void getAllQuotesByQuoteStatus_ShouldSucceed(){

        Quote quote1 = new Quote(
                new PickupAddress("123 Main St", "CityA", Country.CA, "12345", 101, true, "Apartment"),
                new DestinationAddress("456 Oak St", "CityB", Country.USA, "54321", 202, false, "House"),
                new ContactDetails("John", "Doe", "john@example.com", "123-456-7890"),
                ContactMethod.EMAIL,
                LocalDate.of(2023, 1, 1),
                "Additional comments go here",
                "quote1",1500,10000
        );

        Quote quote2 = new Quote(
                new PickupAddress("123 Main St", "CityA", Country.CA, "12345", 101, true, "Apartment"),
                new DestinationAddress("456 Oak St", "CityB", Country.USA, "54321", 202, false, "House"),
                new ContactDetails("John", "Doe", "j@example.com", "123-456-7890"),
                ContactMethod.EMAIL,
                LocalDate.of(2023, 1, 1),
                "Additional comments go here",
                "quote2",2300,10000
        );

        List<Quote> listOfQuotes = List.of(quote1,quote2);


        QuoteResponseModel QRM1 = QuoteResponseModel.builder()
                .quoteId("341dbe66-36b1-4398-b708-dc55aaf60900")
                .pickupStreetAddress("789 Elm St")
                .pickupCity("Villagetown")
                .pickupCountry(Country.USA)
                .pickupPostalCode("67890")
                .pickupNumberOfRooms(3)
                .pickupElevator(false)
                .pickupBuildingType("Condo")
                .destinationStreetAddress("321 Oak St")
                .destinationCity("Cityburgh")
                .destinationCountry(Country.CA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(6)
                .destinationElevator(true)
                .destinationBuildingType("House")
                .firstName("Alice")
                .lastName("Smith")
                .phoneNumber("555-1234")
                .emailAddress("alice.smith@example.com")
                .contactMethod(ContactMethod.PHONE_NUMBER)
                .expectedMovingDate(LocalDate.parse("2023-12-15"))
                .initiationDate(LocalDateTime.parse("2023-12-04T05:08:33.500127636"))
                .comment("I need to move heavy stuff")
                .quoteStatus(QuoteStatus.PENDING)
                .name("Shipment Name1")
                .build();

        QuoteResponseModel QRM2 = QuoteResponseModel.builder()
                .quoteId("341dbe66-36b1-4398-b708-dc55aaf60955")
                .pickupStreetAddress("789 Elm St")
                .pickupCity("Villagetown")
                .pickupCountry(Country.USA)
                .pickupPostalCode("67890")
                .pickupNumberOfRooms(3)
                .pickupElevator(false)
                .pickupBuildingType("Condo")
                .destinationStreetAddress("321 Oak St")
                .destinationCity("Cityburgh")
                .destinationCountry(Country.CA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(6)
                .destinationElevator(true)
                .destinationBuildingType("House")
                .firstName("Alice")
                .lastName("Smith")
                .phoneNumber("555-1234")
                .emailAddress("test@example.com")
                .contactMethod(ContactMethod.PHONE_NUMBER)
                .expectedMovingDate(LocalDate.parse("2023-12-15"))
                .initiationDate(LocalDateTime.parse("2023-12-04T05:08:33.500127636"))
                .comment("I need to move heavy stuff")
                .quoteStatus(QuoteStatus.PENDING)
                .name("Shipment Name2")
                .build();

        List<QuoteResponseModel> listOfQuoteResponseModels = List.of(QRM1,QRM2);

        when(quoteResponseMapper.entitiesListToResponseList(anyList())).thenReturn(listOfQuoteResponseModels);
        List<QuoteResponseModel> convertedQuotesToResponseModels = quoteResponseMapper.entitiesListToResponseList(listOfQuotes);
        assertEquals(convertedQuotesToResponseModels.size(),listOfQuoteResponseModels.size());
        assertEquals(convertedQuotesToResponseModels.get(0).getQuoteStatus(), QuoteStatus.PENDING);



        List<QuoteResponseModel> listOfQuoteResponseModelsResult = quoteService.getAllQuotes(QuoteStatus.PENDING);
        when(quoteService.getAllQuotes(QuoteStatus.PENDING)).thenReturn(listOfQuoteResponseModels);
        assertEquals(listOfQuoteResponseModelsResult.size(),listOfQuoteResponseModels.size());
        assertEquals(listOfQuoteResponseModelsResult.get(0).getQuoteStatus(), QuoteStatus.PENDING);

    }

    @Test
    void declineQuoteEvent_ShouldSucceed_WhenQuoteExists() {
        // Arrange
        String quoteId = "validQuoteId";
        Quote quote = buildQuote();
        quote.setQuoteIdentifier(new QuoteIdentifier());
        quote.setContactDetails(new ContactDetails("John", "Doe", "johndoe@gmail.com", "555-1234"));
        when(quoteRepository.findByQuoteIdentifier_QuoteId(quoteId)).thenReturn(quote);

        // Mocking the templateEngine and emailUtil
        when(templateEngine.process(any(String.class), any())).thenReturn("This is a test email");
        doNothing().when(emailUtil).SslEmail(anyString(), anyString(), anyString());

        // Act
        quoteService.declineQuote(quoteId);

        // Assert
        assertEquals(QuoteStatus.DECLINED, quote.getQuoteStatus());
        verify(quoteRepository).save(quote);

        // Verify that the email content and parameters are as expected
        verify(templateEngine).process(eq("declineQuote"), any(Context.class));
        verify(emailUtil).SslEmail(eq("johndoe@gmail.com"), eq("Quote Declined"), eq("This is a test email"));
    }


    @Test
    void acceptExistingQuoteEvent_ShouldSucceed(){
        //Arrange
        String quoteId="341dbe66-36b1-4398-b708-dc55aaf60955";
        Quote quote=buildQuote();
        quote.setQuoteIdentifier(new QuoteIdentifier());
        when(quoteRepository.findByQuoteIdentifier_QuoteId(quoteId)).thenReturn(quote);

        //Act
        quoteService.acceptQuote(quoteId);

        //Assert
        assertEquals(QuoteStatus.ACCEPTED, quote.getQuoteStatus());
        verify(quoteRepository).save(quote);
    }

    @Test
    void convertExistingQuoteEventToShipment_ShouldSucceed(){
        //Arrange
        String quoteId="341dbe66-36b1-4398-b708-dc55aaf60955";
        Quote quote=buildQuote();
        quote.setQuoteIdentifier(new QuoteIdentifier());
        when(quoteRepository.findByQuoteIdentifier_QuoteId(quoteId)).thenReturn(quote);

        //Act
        quoteService.convertQuoteToShipment(quoteId);

        //Assert
        assertEquals(QuoteStatus.CREATED, quote.getQuoteStatus());
        verify(quoteRepository).save(quote);
    }

    @Test
    void declineQuote_ShouldThrowIllegalArgumentException_WhenInvalidStatusIsSet() {
        // Arrange
        String quoteId = "validQuoteId";
        String invalidStatus = "InvalidStatus";
        Quote quote = buildQuote();
        when(quoteRepository.findByQuoteIdentifier_QuoteId(quoteId)).thenReturn(quote);


        doAnswer(invocation -> {
            throw new IllegalArgumentException("Unexpected event value: " + invalidStatus);
        }).when(quoteRepository).save(any(Quote.class));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            quoteService.declineQuote(quoteId);
        });

        // Assert
        assertThat(exception.getMessage().contains("Unexpected event value: " + invalidStatus));
    }



    private Quote buildQuote(){
        return Quote.builder()
                .pickupAddress(
                        PickupAddress.builder()
                                .pickupStreetAddress("123 Main St")
                                .pickupCity("Anytown")
                                .pickupCountry(Country.USA)
                                .pickupPostalCode("12345")
                                .pickupNumberOfRooms(5)
                                .pickupElevator(true)
                                .pickupBuildingType("Apartment")
                                .build()
                )
                .destinationAddress(
                        DestinationAddress.builder()
                                .destinationStreetAddress("456 Market St")
                                .destinationCity("Othertown")
                                .destinationCountry(Country.CA)
                                .destinationPostalCode("54321")
                                .destinationNumberOfRooms(4)
                                .destinationElevator(false)
                                .destinationBuildingType("House")
                                .build()
                )
                .contactDetails(
                        ContactDetails.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .emailAddress("johndoe@example.com")
                                .phoneNumber("555-1234")
                                .build()
                )
                .contactMethod(ContactMethod.EMAIL)
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .initiationDate(LocalDateTime.now())
                .comment("Moving soon")
                .quoteStatus(QuoteStatus.PENDING)
                .shipmentName("Sample Shipment")
                .approximateWeight(1740.0)
                .approximateShipmentValue(15000.0)
                .build();
    }

    private QuoteResponseModel buildQuoteResponse(){
        return QuoteResponseModel.builder()
                .quoteId("341dbe66-36b1-4398-b708-dc55aaf60986")
                .pickupStreetAddress("123 Main St")
                .pickupCity("Anytown")
                .pickupCountry(Country.USA)
                .pickupPostalCode("12345")
                .pickupNumberOfRooms(5)
                .pickupElevator(true)
                .pickupBuildingType("Apartment")
                .destinationStreetAddress("456 Market St")
                .destinationCity("Othertown")
                .destinationCountry(Country.CA)
                .destinationPostalCode("54321")
                .destinationNumberOfRooms(4)
                .destinationElevator(false)
                .destinationBuildingType("House")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("555-1234")
                .emailAddress("johndoe@example.com")
                .contactMethod(ContactMethod.EMAIL)
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .initiationDate(LocalDateTime.now())
                .comment("Moving soon")
                .quoteStatus(QuoteStatus.PENDING)
                .name("Sample Shipment")
                .build();
    }
}

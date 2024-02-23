package com.example.backend.shipmentsubdomain.datalayer;

import com.example.backend.shipmentsubdomain.datalayer.quote.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuoteRepositoryPersistenceTest {

    @Autowired
    private QuoteRepository quoteRepository;
    private Quote savedQuote;

    @BeforeEach
    public void setUp() {
        quoteRepository.deleteAll();
    }

    @Test
    public void findByQuoteIdentifier_QuoteId_shouldSucceed(){
        savedQuote=quoteRepository.save(buildQuote());

        //act
        Quote found=quoteRepository.findByQuoteIdentifier_QuoteId(savedQuote.getQuoteIdentifier().getQuoteId());

        //assert
        assertNotNull(found);
        assertThat(found,samePropertyValuesAs(savedQuote));
    }

    @AfterEach
    public void tearDown() {
        quoteRepository.deleteAll();
    }

    @Test
    public void whenSaveQuoteWithValues_thenValuesAreCorrectlyPersisted() {
        // Arrange: Create a sample Quote
        Quote quote = new Quote(
                new PickupAddress("123 Main St", "CityA", Country.CA, "12345", 101, true, "Apartment"),
                new DestinationAddress("456 Oak St", "CityB", Country.USA, "54321", 202, false, "House"),
                new ContactDetails("John", "Doe", "john.doe@example.com", "123-456-7890"),
                ContactMethod.EMAIL,
                LocalDate.of(2023, 1, 1),
                "Additional comments go here",
                "Moving out of parents house"
                ,0.0,0.0
        );

        // Act
        Quote savedQuote = quoteRepository.save(quote);

        // Assert and check
        assertThat(savedQuote.getExpectedMovingDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(savedQuote.getContactMethod()).isEqualTo(ContactMethod.EMAIL);
        assertThat(savedQuote.getComment()).isEqualTo("Additional comments go here");

        assertThat(savedQuote.getPickupAddress().getPickupStreetAddress()).isEqualTo("123 Main St");
        assertThat(savedQuote.getPickupAddress().getPickupCity()).isEqualTo("CityA");
        assertThat(savedQuote.getPickupAddress().getPickupCountry()).isEqualTo(Country.CA);
        assertThat(savedQuote.getPickupAddress().getPickupPostalCode()).isEqualTo("12345");
        assertThat(savedQuote.getPickupAddress().getPickupNumberOfRooms()).isEqualTo(101);
        assertThat(savedQuote.getPickupAddress().isPickupElevator()).isTrue();
        assertThat(savedQuote.getPickupAddress().getPickupBuildingType()).isEqualTo("Apartment");

        assertThat(savedQuote.getDestinationAddress().getDestinationStreetAddress()).isEqualTo("456 Oak St");
        assertThat(savedQuote.getDestinationAddress().getDestinationCity()).isEqualTo("CityB");
        assertThat(savedQuote.getDestinationAddress().getDestinationCountry()).isEqualTo(Country.USA);
        assertThat(savedQuote.getDestinationAddress().getDestinationPostalCode()).isEqualTo("54321");
        assertThat(savedQuote.getDestinationAddress().getDestinationNumberOfRooms()).isEqualTo(202);
        assertThat(savedQuote.getDestinationAddress().isDestinationElevator()).isFalse();
        assertThat(savedQuote.getDestinationAddress().getDestinationBuildingType()).isEqualTo("House");

        assertThat(savedQuote.getContactDetails().getFirstName()).isEqualTo("John");
        assertThat(savedQuote.getContactDetails().getLastName()).isEqualTo("Doe");
        assertThat(savedQuote.getContactDetails().getEmailAddress()).isEqualTo("john.doe@example.com");
        assertThat(savedQuote.getContactDetails().getPhoneNumber()).isEqualTo("123-456-7890");
    }

    @Test
    public void findAllClientByStatus_shouldSucceed(){
        // predicted size
        int expectedNumberOfPendingQuotes = 2;

        //Create a sample Quote
        Quote quote1 = new Quote(
                new PickupAddress("123 Main St", "CityA", Country.CA, "12345", 101, true, "Apartment"),
                new DestinationAddress("456 Oak St", "CityB", Country.USA, "54321", 202, false, "House"),
                new ContactDetails("John", "Doe", "john.doe@example.com", "123-456-7890"),
                ContactMethod.EMAIL,
                LocalDate.of(2023, 1, 1),
                "Additional comments go here",
                "Moving out of parents house",
                0.0,
                0.0
        );

        Quote quote2 = new Quote(
                new PickupAddress("123 Main St", "CityA", Country.CA, "12345", 101, true, "Apartment"),
                new DestinationAddress("456 Oak St", "CityB", Country.USA, "54321", 202, false, "House"),
                new ContactDetails("John", "Doe", "j@example.com", "123-456-7890"),
                ContactMethod.EMAIL,
                LocalDate.of(2023, 1, 1),
                "Additional comments go here",
                "Moving out of parents house",
                0.0,
                0.0
        );

        // save quote
        Quote saved1 = quoteRepository.save(quote1);
        Quote saved2 = quoteRepository.save(quote2);

        assertEquals(saved1.getQuoteStatus(),QuoteStatus.PENDING);
        assertEquals(saved2.getQuoteStatus(),QuoteStatus.PENDING);

        // get all the list of quotes that are pending
        List<Quote> listOfQuotesPending = quoteRepository.findAllByQuoteStatus(QuoteStatus.PENDING);

        //assert that list gotten is the same size as the predicted size
        assertEquals(expectedNumberOfPendingQuotes,listOfQuotesPending.size());

    }


    @Test
    public void whenDeclineEvent_thenQuoteStatusUpdatedToDeclined() {
        // Arrange
        Quote quote = buildQuote();
        quote = quoteRepository.save(quote);
        String quoteId = quote.getQuoteIdentifier().getQuoteId();

        // Act
        Quote retrievedQuote = quoteRepository.findByQuoteIdentifier_QuoteId(quoteId);
        retrievedQuote.setQuoteStatus(QuoteStatus.DECLINED);
        quoteRepository.save(retrievedQuote);

        // Assert
        Quote updatedQuote = quoteRepository.findByQuoteIdentifier_QuoteId(quoteId);
        assertNotNull(updatedQuote);
        assertEquals(QuoteStatus.DECLINED, updatedQuote.getQuoteStatus());
    }


    @Test
    public void whenEventWithInvalidStatus_thenThrowIllegalArgumentException() {
        String invalidStatus = "Invalid";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Quote quote = buildQuote();
            quote.setQuoteStatus(QuoteStatus.valueOf(invalidStatus));
            quoteRepository.save(quote);
        });

        // Assert:
        String expectedMessage = "Unexpected event value: " + invalidStatus;
        assertThat(exception.getMessage().contains(expectedMessage));
    }

    private Quote buildQuote(){
        return Quote.builder()
                .quoteIdentifier(new QuoteIdentifier())
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
                .build();
    }
}
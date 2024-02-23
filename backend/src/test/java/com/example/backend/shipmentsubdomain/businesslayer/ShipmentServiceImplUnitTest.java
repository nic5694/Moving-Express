package com.example.backend.shipmentsubdomain.businesslayer;

import com.example.backend.shipmentsubdomain.businesslayer.shipment.ShipmentServiceImpl;
import com.example.backend.shipmentsubdomain.datalayer.Address.Address;
import com.example.backend.shipmentsubdomain.datalayer.Address.AddressRepository;
import com.example.backend.shipmentsubdomain.datalayer.observer.Observer;
import com.example.backend.shipmentsubdomain.datalayer.observer.ObserverRepository;
import com.example.backend.shipmentsubdomain.datalayer.observer.Permission;
import com.example.backend.shipmentsubdomain.datalayer.quote.ContactMethod;
import com.example.backend.shipmentsubdomain.datalayer.Country;
import com.example.backend.shipmentsubdomain.datalayer.quote.QuoteStatus;
import com.example.backend.shipmentsubdomain.datalayer.shipment.*;
import com.example.backend.shipmentsubdomain.datalayer.truck.TruckIdentifier;
import com.example.backend.shipmentsubdomain.datamapperlayer.shipment.AddressMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.shipment.QuoteResponseToShipmentMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.shipment.ShipmentRequestMapper;
import com.example.backend.shipmentsubdomain.datamapperlayer.shipment.ShipmentResponseMapper;
import com.example.backend.shipmentsubdomain.presentationlayer.quote.QuoteResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.AddressResponseModel;
import com.example.backend.shipmentsubdomain.presentationlayer.shipment.ShipmentResponseModel;
import com.example.backend.usersubdomain.buisnesslayer.UserService;
import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.presentationlayer.UserResponseModel;
import com.example.backend.util.EmailUtil;
import com.example.backend.util.exceptions.InvalidOperationException;
import com.example.backend.util.exceptions.ShipmentNotFoundException;
import com.example.backend.util.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class ShipmentServiceImplUnitTest {
    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private ShipmentRequestMapper shipmentRequestMapper;
    @Mock
    private ShipmentResponseMapper shipmentResponseMapper;
    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Mock
    private QuoteResponseToShipmentMapper quoteResponseToShipmentMapper;
    @Mock
    private AddressMapper addressMapper;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private EmailUtil emailUtil;

    private Shipment mockShipment;

    private ShipmentResponseModel mockShipmentResponse;

    private AddressResponseModel addressResponseModel;
    @Mock
    private ObserverRepository observerRepository;
    @Mock
    private UserService userService;

    private UserResponseModel userResponseModel;


    @BeforeEach
    void setUp() {
        Address pickupAddress = Address.builder()
                .streetAddress("123 Pickup St")
                .city("PickupCity")
                .postalCode("PC123")
                .country(Country.USA)
                .build();

        Address destinationAddress = Address.builder()
                .streetAddress("456 Destination Ave")
                .city("DestinationCity")
                .postalCode("PC456")
                .country(Country.USA)
                .build();

        TruckIdentifier truckIdentifier = new TruckIdentifier("1A8HW58268F113778");
        Status status = Status.QUOTED;

        mockShipment = new Shipment(
                "user123",
                truckIdentifier,
                status,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 5),
                2000.0,
                "Sample Shipment",
                pickupAddress,
                destinationAddress,
                "email@example.com",
                "1234567890",
                1000.0,
                null);

        AddressResponseModel pickupAddressResponse = convertToAddressResponseModel(pickupAddress);
        AddressResponseModel destinationAddressResponse = convertToAddressResponseModel(destinationAddress);

        mockShipmentResponse = ShipmentResponseModel.builder()
                .shipmentId("shipment123")
                .pickupAddress(pickupAddressResponse)
                .destinationAddress(destinationAddressResponse)
                .userId("user123")
                .truckId("truck123")
                .status(status)
                .shipmentName("Sample Shipment")
                .approximateWeight(2000.0)
                .weight(1950.0)
                .email("email@example.com")
                .phoneNumber("1234567890")
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .actualMovingDate(LocalDate.of(2023, 1, 5))
                .firstName("John")
                .lastName("Doe")
                .approximateShipmentValue(1000.0)
                .build();

        userResponseModel = UserResponseModel.builder()
                .userId("user123")
                .profilePictureUrl("https://example.com")
                .email("user@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();
    }

    private AddressResponseModel convertToAddressResponseModel(Address address) {
        return AddressResponseModel.builder()
                .streetAddress(address.getStreetAddress())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
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
    void getAllShipmentsWithUserId() {
        String userId = "user123";
        when(shipmentRepository.findShipmentByUserId(userId)).thenReturn(Collections.singletonList(mockShipment));
        when(shipmentResponseMapper.entityToResponseModel(mockShipment)).thenReturn(mockShipmentResponse);

        List<ShipmentResponseModel> result = shipmentService.getAllShipments(Optional.of(userId), Optional.empty());

        verify(shipmentRepository).findShipmentByUserId(userId);
        verify(shipmentResponseMapper).entityToResponseModel(mockShipment);
        assertEquals(1, result.size());
        assertEquals(mockShipmentResponse, result.get(0));
    }

    @Test
    void getAllShipmentsWithEmail() {
        String email = "user@example.com";
        when(shipmentRepository.findShipmentByEmail(email)).thenReturn(Collections.singletonList(mockShipment));
        when(shipmentResponseMapper.entityToResponseModel(mockShipment)).thenReturn(mockShipmentResponse);

        List<ShipmentResponseModel> result = shipmentService.getAllShipments(Optional.empty(), Optional.of(email));

        verify(shipmentRepository).findShipmentByEmail(email);
        verify(shipmentResponseMapper).entityToResponseModel(mockShipment);
        assertEquals(1, result.size());
        assertEquals(mockShipmentResponse, result.get(0));
    }

    @Test
    void getAllShipmentsWhenNeitherUserIdNorEmailIsProvided() {
        when(shipmentRepository.findAll()).thenReturn(Collections.singletonList(mockShipment));
        when(shipmentResponseMapper.entityToResponseModel(mockShipment)).thenReturn(mockShipmentResponse);

        List<ShipmentResponseModel> result = shipmentService.getAllShipments(Optional.empty(), Optional.empty());

        verify(shipmentRepository).findAll();
        verify(shipmentResponseMapper).entityToResponseModel(mockShipment);
        assertEquals(1, result.size());
        assertEquals(mockShipmentResponse, result.get(0));
    }

    @Test
    void getShipmentWithValidShipmentId_ShouldSucceed() {
        Shipment existingShipment = buildShipment();
        ShipmentResponseModel shipmentResponseModel = buildShipmentResponseModel(existingShipment.getShipmentIdentifier(), existingShipment.getTruckIdentifier());

        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(existingShipment);
        when(shipmentResponseMapper.entityToResponseModel(existingShipment)).thenReturn(shipmentResponseModel);

        ShipmentResponseModel result = shipmentService.getShipment("c760a2f1-1981-4efb-83d1-3618f363ed27");

        assertEquals(result.getUserId(), existingShipment.getUserId());
        assertEquals(result.getStatus(), existingShipment.getStatus());
        assertEquals(result.getExpectedMovingDate(), existingShipment.getExpectedMovingDate());
        assertEquals(result.getActualMovingDate(), existingShipment.getActualMovingDate());
        assertEquals(result.getApproximateWeight(), existingShipment.getApproximateWeight());
        assertEquals(result.getShipmentName(), existingShipment.getName());
        assertEquals(convertToAddress(result.getPickupAddress()), existingShipment.getPickupAddress());
        assertEquals(convertToAddress(result.getDestinationAddress()), existingShipment.getDestinationAddress());
        assertEquals(result.getEmail(), existingShipment.getEmail());
        assertEquals(result.getPhoneNumber(), existingShipment.getPhoneNumber());
        assertEquals(result.getWeight(), existingShipment.getWeight());
    }

    @Test
    void deleteShipmentWithValidShipmentId_ShouldSucceed(){
        //arrange
        Shipment existingShipment=buildShipment();
        when(templateEngine.process(any(String.class), any())).thenReturn("This is a test email");
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(existingShipment);
        when(shipmentRepository.save(existingShipment)).thenReturn(existingShipment);
        shipmentService.cancelShipment(existingShipment.getShipmentIdentifier().getShipmentId());
        doNothing().when(emailUtil).SslEmail(anyString(), anyString(), anyString());
        //assert
        assertEquals(existingShipment.getStatus(), Status.CANCELED);
    }

    @Test
    void deleteShipmentWithInvalidShipmentId_ShouldReturnShipmentNotFoundException(){
        //arrange
        Shipment existingShipment=buildShipment();
        when(templateEngine.process(any(String.class), any())).thenReturn("This is a test email");
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(null);
        when(shipmentRepository.save(existingShipment)).thenReturn(existingShipment);
        //assert
        try{
            shipmentService.cancelShipment(existingShipment.getShipmentIdentifier().getShipmentId());
        }catch(ShipmentNotFoundException e){
            assertEquals(e.getMessage(), "shipmentId not found: "+existingShipment.getShipmentIdentifier().getShipmentId());
        }
    }

    @Test
    void deleteShipmentWithShipmentStatusAsTransit_ShouldReturn_InvalidOperationException(){
        //arrange
        Shipment existingShipment=buildShipment();
        existingShipment.setStatus(Status.TRANSIT);
        when(templateEngine.process(any(String.class), any())).thenReturn("This is a test email");
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(existingShipment);
        when(shipmentRepository.save(existingShipment)).thenReturn(existingShipment);
        //assert
        try{
            shipmentService.cancelShipment(existingShipment.getShipmentIdentifier().getShipmentId());
        }catch(InvalidOperationException e){
            assertEquals(e.getMessage(), "Shipment cannot be cancelled while it is in transit.");
        }
    }

    @Test
    void deleteShipmentWithShipmentStatusAsDelivered_ShouldReturn_InvalidOperationException(){
        //arrange
        Shipment existingShipment=buildShipment();
        existingShipment.setStatus(Status.DELIVERED);
        when(templateEngine.process(any(String.class), any())).thenReturn("This is a test email");
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(existingShipment);
        when(shipmentRepository.save(existingShipment)).thenReturn(existingShipment);
        //assert
        try{
            shipmentService.cancelShipment(existingShipment.getShipmentIdentifier().getShipmentId());
        }catch(InvalidOperationException e){
            assertEquals(e.getMessage(), "Shipment cannot be cancelled after it has been delivered.");
        }
    }


    @Test
    void createShipment() {
        TruckIdentifier truckIdentifier = new TruckIdentifier("1A8HW58268F113778");

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
                .approximateShipmentValue(19000)
                .build();

        Address departureAddress = new Address(quoteResponseModel.getPickupStreetAddress(),
                quoteResponseModel.getPickupCity(),
                quoteResponseModel.getPickupCountry(),
                quoteResponseModel.getPickupPostalCode());

        Address destinationAddress = new Address(quoteResponseModel.getDestinationStreetAddress(),
                quoteResponseModel.getDestinationCity(),
                quoteResponseModel.getDestinationCountry(),
                quoteResponseModel.getDestinationPostalCode());

        AddressResponseModel pickupAddressResponse = convertToAddressResponseModel(departureAddress);
        AddressResponseModel destinationAddressResponse = convertToAddressResponseModel(destinationAddress);

        ShipmentResponseModel shipmentResponseModel = ShipmentResponseModel.builder()
                .shipmentId("shipmentId")
                .pickupAddress(pickupAddressResponse) // replace with actual Address
                .destinationAddress(destinationAddressResponse) // replace with actual Address
                .userId("userId")
                .truckId(truckIdentifier.toString())
                .status(Status.QUOTED)
                .shipmentName("shipmentName")
                .weight(180000)
                .email("email@example.com")
                .approximateWeight(180000)
                .approximateShipmentValue(19000)
                .build();

        Shipment shipment = new Shipment();
        shipment.setShipmentIdentifier(new ShipmentIdentifier());
        shipment.setPickupAddress(departureAddress);
        shipment.setDestinationAddress(destinationAddress);
        shipment.setUserId(shipmentResponseModel.getUserId());
        shipment.setTruckIdentifier(truckIdentifier);
        shipment.setStatus(shipmentResponseModel.getStatus());
        shipment.setName(shipmentResponseModel.getShipmentName());
        shipment.setApproximateWeight(shipmentResponseModel.getApproximateWeight());
        shipment.setWeight(shipmentResponseModel.getWeight());

        when(addressMapper.toAddress(quoteResponseModel.getPickupStreetAddress(),
                quoteResponseModel.getPickupCity(),
                quoteResponseModel.getPickupPostalCode(),
                quoteResponseModel.getPickupCountry())).thenReturn(departureAddress);
        when(addressRepository.save(departureAddress)).thenReturn(departureAddress);
        when(addressMapper.toAddress(quoteResponseModel.getDestinationStreetAddress(),
                quoteResponseModel.getDestinationCity(),
                quoteResponseModel.getDestinationPostalCode(),
                quoteResponseModel.getDestinationCountry())).thenReturn(destinationAddress);
        when(addressRepository.save(destinationAddress)).thenReturn(destinationAddress);

        when(userService.getUserByEmail(userResponseModel.getEmail())).thenReturn(userResponseModel);
        when(shipmentService.generateShipmentConfirmationEmailContentString(shipment.getShipmentIdentifier().getShipmentId())).thenReturn("This is a test email");
        //commented out to fix the test
       // doNothing().when(emailUtil).SslEmail(quoteResponseModel.getEmailAddress(), "Shipment Creation Confirmation", shipmentService.generateShipmentConfirmationEmailContentString(shipment.getShipmentIdentifier().getShipmentId()));
        when(quoteResponseToShipmentMapper.toShipment(quoteResponseModel, addressMapper)).thenReturn(shipment);
        shipment.setPickupAddress(departureAddress);
        shipment.setDestinationAddress(destinationAddress);
        shipment.setStatus(Status.QUOTED);
        shipment.setEmail(quoteResponseModel.getEmailAddress());
        shipment.setPhoneNumber(quoteResponseModel.getPhoneNumber());
        shipment.setUserId(userResponseModel.getUserId());
        shipment.setFirstName(userResponseModel.getFirstName());
        shipment.setLastName(userResponseModel.getLastName());

        when(shipmentRepository.save(shipment)).thenReturn(shipment);
        when(shipmentResponseMapper.entityToResponseModel(shipment)).thenReturn(shipmentResponseModel);
        shipmentService.createShipment(quoteResponseModel);
        verify(addressMapper, Mockito.times(1)).toAddress(
                quoteResponseModel.getPickupStreetAddress(),
                quoteResponseModel.getPickupCity(),
                quoteResponseModel.getPickupPostalCode(),
                quoteResponseModel.getPickupCountry());
        verify(addressRepository, Mockito.times(1)).save(departureAddress);
        verify(addressMapper, Mockito.times(1)).toAddress(
                quoteResponseModel.getDestinationStreetAddress(),
                quoteResponseModel.getDestinationCity(),
                quoteResponseModel.getDestinationPostalCode(),
                quoteResponseModel.getDestinationCountry());
        verify(addressRepository, Mockito.times(1)).save(destinationAddress);
        verify(shipmentRepository, Mockito.times(1)).save(shipment);
        verify(quoteResponseToShipmentMapper, Mockito.times(1)).toShipment(quoteResponseModel, addressMapper);
        verify(shipmentResponseMapper, Mockito.times(1)).entityToResponseModel(shipment);
    }

    @Test
    void cancelShipmentWithShipmentIdThatIsAlreadyCancelled_ShouldThrowInvalidOperationException(){
        //arrange
        Shipment existingShipment=buildShipment();
        existingShipment.setStatus(Status.CANCELED);
        when(templateEngine.process(any(String.class), any())).thenReturn("This is a test email");
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(anyString())).thenReturn(existingShipment);
        when(shipmentRepository.save(existingShipment)).thenReturn(existingShipment);
        //assert
        try{
            shipmentService.cancelShipment(existingShipment.getShipmentIdentifier().getShipmentId());
        }catch(InvalidOperationException e){
            assertEquals(e.getMessage(), "Shipment has already been cancelled.");
        }
    }

    @Test
    void getShipmentDetailsByObserverCode_ValidObserverCode_ShouldReturnShipmentDetails() {
        // Arrange
        String observerCode = "VALIDCODE";
        Observer mockObserver = new Observer("shipment123", "Observer Name", observerCode, Permission.READ);
        when(observerRepository.findObserverByObserverCode(observerCode)).thenReturn(mockObserver);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId("shipment123")).thenReturn(mockShipment);
        when(shipmentResponseMapper.entityToResponseModel(mockShipment)).thenReturn(mockShipmentResponse);

        // Act
        ShipmentResponseModel result = shipmentService.getShipmentDetailsByObserverCode(observerCode);

        // Assert
        assertEquals("shipment123", result.getShipmentId());
        verify(observerRepository).findObserverByObserverCode(observerCode);
        verify(shipmentRepository).findShipmentByShipmentIdentifier_ShipmentId("shipment123");
    }

    @Test
    void getShipmentDetailsByObserverCode_InvalidObserverCode_ShouldThrowShipmentNotFoundException() {
        // Arrange
        String observerCode = "INVALIDCODE";
        when(observerRepository.findObserverByObserverCode(observerCode)).thenReturn(null);

        // Act & Assert
        assertThrows(ShipmentNotFoundException.class, () -> {
            shipmentService.getShipmentDetailsByObserverCode(observerCode);
        });
    }

    @Test
    void getShipmentDetailsByObserverCode_ValidObserverCodeButNoShipment_ShouldThrowShipmentNotFoundException() {
        // Arrange
        String observerCode = "VALIDCODE";
        Observer mockObserver = new Observer("invalidShipmentId", "Observer Name", observerCode, Permission.READ);
        when(observerRepository.findObserverByObserverCode(observerCode)).thenReturn(mockObserver);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId("invalidShipmentId")).thenReturn(null);

        // Act & Assert
        assertThrows(ShipmentNotFoundException.class, () -> {
            shipmentService.getShipmentDetailsByObserverCode(observerCode);
        });
    }

    @Test
    void getShipmentsByDriverId_InvalidDriverId_ShouldThrowUserNotFoundException(){
        //arrange
        String driverId="invalidDriverId";
        when(userService.getUserByUserId(driverId)).thenReturn(null);
        //assert
        assertThrows(UserNotFoundException.class, ()->{
            shipmentService.getShipmentsByDriverId(driverId);
        });
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
                        .streetAddress("123 Pickup St") // Matched with the response model
                        .country(Country.USA) // Matched with the response model
                        .postalCode("PC123") // Matched with the response model
                        .build())
                .destinationAddress(Address.builder()
                        .city("DestinationCity")
                        .streetAddress("456 Destination Ave") // Assuming similar format
                        .country(Country.USA) // Assuming similar format
                        .postalCode("PC456") // Assuming similar format
                        .build())
                .email("example@example.com")
                .phoneNumber("1234567890")
                .approximateWeight(180000)
                .approximateShipmentValue(19000)
                .truckIdentifier(new TruckIdentifier())
                .driverId("auth|987654321")
                .build();
    }

    private ShipmentResponseModel buildShipmentResponseModel(ShipmentIdentifier shipmentIdentifier, TruckIdentifier truckIdentifier) {
        AddressResponseModel pickupAddressResponse = convertToAddressResponseModel(
                Address.builder()
                        .city("PickupCity")
                        .streetAddress("123 Pickup St")
                        .country(Country.USA)
                        .postalCode("PC123")
                        .build());

        AddressResponseModel destinationAddressResponse = convertToAddressResponseModel(
                Address.builder()
                        .city("DestinationCity")
                        .streetAddress("456 Destination Ave")
                        .country(Country.USA)
                        .postalCode("PC456")
                        .build());

        return ShipmentResponseModel.builder()
                .userId("exampleUserId")
                .status(Status.QUOTED)
                .shipmentId(shipmentIdentifier.getShipmentId())
                .expectedMovingDate(LocalDate.of(2023, 1, 1))
                .actualMovingDate(LocalDate.of(2023, 1, 5))
                .pickupAddress(pickupAddressResponse)
                .destinationAddress(destinationAddressResponse)
                .email("example@example.com")
                .phoneNumber("1234567890")
                .shipmentName("Example Shipment")
                .approximateWeight(100.0)
                .weight(0.0)
                .truckId(truckIdentifier.getVin())
                .approximateWeight(180000)
                .approximateShipmentValue(19000)
                .build();
    }

    @Test
    void getUnassignedShipments_ShouldSucceed(){
        Shipment shipment1=buildShipment();
        Shipment shipment2=buildShipment();
        List<Shipment> shipments=new ArrayList<>();
        shipments.add(shipment1);
        shipments.add(shipment2);

        ShipmentResponseModel shipmentResponseModel1=buildShipmentResponseModel(shipment1.getShipmentIdentifier(), shipment1.getTruckIdentifier());
        ShipmentResponseModel shipmentResponseModel2=buildShipmentResponseModel(shipment2.getShipmentIdentifier(), shipment2.getTruckIdentifier());
        List<ShipmentResponseModel> shipmentResponseModels=new ArrayList<>();
        shipmentResponseModels.add(shipmentResponseModel1);
        shipmentResponseModels.add(shipmentResponseModel2);

        when(shipmentRepository.findAllByDriverIdIsNull()).thenReturn(shipments);
        when(shipmentResponseMapper.entitiesToResponseModel(shipments)).thenReturn(shipmentResponseModels);

        List<ShipmentResponseModel> result = shipmentService.getUnassignedShipments();

        assertEquals(result.get(0).getUserId(), shipment1.getUserId());
        assertEquals(result.get(0).getStatus(), shipment1.getStatus());
        assertEquals(result.get(0).getExpectedMovingDate(), shipment1.getExpectedMovingDate());
        assertEquals(result.get(0).getActualMovingDate(), shipment1.getActualMovingDate());
        assertEquals(result.get(0).getApproximateWeight(), shipment1.getApproximateWeight());
        assertEquals(result.get(0).getShipmentName(), shipment1.getName());
        assertEquals(convertToAddress(result.get(0).getPickupAddress()), shipment1.getPickupAddress());
        assertEquals(convertToAddress(result.get(0).getDestinationAddress()), shipment1.getDestinationAddress());
        assertEquals(result.get(0).getEmail(), shipment1.getEmail());
        assertEquals(result.get(0).getPhoneNumber(), shipment1.getPhoneNumber());
        assertEquals(result.get(0).getApproximateWeight(), shipment1.getApproximateWeight());
        assertEquals(result.get(0).getApproximateShipmentValue(), shipment1.getApproximateShipmentValue());

        assertEquals(result.get(1).getUserId(), shipment2.getUserId());
        assertEquals(result.get(1).getStatus(), shipment2.getStatus());
        assertEquals(result.get(1).getExpectedMovingDate(), shipment2.getExpectedMovingDate());
        assertEquals(result.get(1).getActualMovingDate(), shipment2.getActualMovingDate());
        assertEquals(result.get(1).getApproximateWeight(), shipment2.getApproximateWeight());
        assertEquals(result.get(1).getShipmentName(), shipment2.getName());
        assertEquals(convertToAddress(result.get(1).getPickupAddress()), shipment2.getPickupAddress());
        assertEquals(convertToAddress(result.get(1).getDestinationAddress()), shipment2.getDestinationAddress());
        assertEquals(result.get(1).getEmail(), shipment2.getEmail());
        assertEquals(result.get(1).getPhoneNumber(), shipment2.getPhoneNumber());
        assertEquals(result.get(1).getApproximateWeight(), shipment2.getApproximateWeight());
        assertEquals(result.get(1).getApproximateShipmentValue(), shipment2.getApproximateShipmentValue());
    }
    
    @Test
    void unassignShipmentByValidDriverId_ShouldSucceed(){
        String userId = "auth|123456789";
        UserResponseModel mockDriverResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        Shipment shipment=buildShipment();
        ShipmentResponseModel shipmentResponseModel=buildShipmentResponseModel(shipment.getShipmentIdentifier(), shipment.getTruckIdentifier());

        when(userService.getUserByUserId(userId)).thenReturn(mockDriverResponse);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(shipmentRepository.save(shipment)).thenReturn(shipment);
        when(shipmentResponseMapper.entityToResponseModel(shipment)).thenReturn(shipmentResponseModel);

        ShipmentResponseModel result=shipmentService.unassignShipment(shipment.getShipmentIdentifier().getShipmentId(), userId);

        assertEquals(result.getUserId(), shipment.getUserId());
        assertEquals(result.getStatus(), shipment.getStatus());
        assertEquals(result.getExpectedMovingDate(), shipment.getExpectedMovingDate());
        assertEquals(result.getActualMovingDate(), shipment.getActualMovingDate());
        assertEquals(result.getApproximateWeight(), shipment.getApproximateWeight());
        assertEquals(result.getShipmentName(), shipment.getName());
        assertEquals(convertToAddress(result.getPickupAddress()), shipment.getPickupAddress());
        assertEquals(convertToAddress(result.getDestinationAddress()), shipment.getDestinationAddress());
        assertEquals(result.getEmail(), shipment.getEmail());
        assertEquals(result.getPhoneNumber(), shipment.getPhoneNumber());
        assertEquals(result.getWeight(), shipment.getWeight());
        assertNull(result.getDriverId());
        assertNull(result.getTruckId());
    }

    @Test
    void unassignShipmentWithShipmentStatusAsLoading_ShouldThrowInvalidOperationException(){
        String userId = "auth|123456789";
        UserResponseModel mockDriverResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        Shipment shipment=buildShipment();
        shipment.setStatus(Status.LOADING);

        when(userService.getUserByUserId(userId)).thenReturn(mockDriverResponse);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(shipmentRepository.save(shipment)).thenReturn(shipment);

        try{
            shipmentService.unassignShipment(shipment.getShipmentIdentifier().getShipmentId(), userId);
        }catch(InvalidOperationException e){
            assertEquals(e.getMessage(), "Shipment cannot be unassigned while it is loading.");
        }
    }

    @Test
    void unassignShipmentWithShipmentStatusAsTransit_ShouldThrowInvalidOperationException(){
        String userId = "auth|123456789";
        UserResponseModel mockDriverResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        Shipment shipment=buildShipment();
        shipment.setStatus(Status.TRANSIT);

        when(userService.getUserByUserId(userId)).thenReturn(mockDriverResponse);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(shipmentRepository.save(shipment)).thenReturn(shipment);

        try{
            shipmentService.unassignShipment(shipment.getShipmentIdentifier().getShipmentId(), userId);
        }catch(InvalidOperationException e){
            assertEquals(e.getMessage(), "Shipment cannot be unassigned while it is in transit.");
        }
    }

    @Test
    void unassignShipmentWithShipmentStatusAsDelivered_ShouldThrowInvalidOperationException(){
        String userId = "auth|123456789";
        UserResponseModel mockDriverResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        Shipment shipment=buildShipment();
        shipment.setStatus(Status.DELIVERED);

        when(userService.getUserByUserId(userId)).thenReturn(mockDriverResponse);
        when(shipmentRepository.findShipmentByShipmentIdentifier_ShipmentId(shipment.getShipmentIdentifier().getShipmentId())).thenReturn(shipment);
        when(shipmentRepository.save(shipment)).thenReturn(shipment);

        try{
            shipmentService.unassignShipment(shipment.getShipmentIdentifier().getShipmentId(), userId);
        }catch(InvalidOperationException e){
            assertEquals(e.getMessage(), "Shipment cannot be unassigned after it has been delivered.");
        }
    }
}
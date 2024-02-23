package com.example.backend.usersubdomain.buisnesslayer;

import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.datalayer.UserRepository;
import com.example.backend.usersubdomain.datamapperlayer.UserRequestMapper;
import com.example.backend.usersubdomain.datamapperlayer.UserResponseMapper;
import com.example.backend.usersubdomain.presentationlayer.UserRequestModel;
import com.example.backend.usersubdomain.presentationlayer.UserResponseModel;
import com.example.backend.util.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserResponseMapper userResponseMapper;
    @Mock
    private UserRequestMapper userRequestMapper;
    @InjectMocks
    private UserServiceImpl customerService;

    @Test
    void getCustomerByUserId() {
        String userId = "auth|123456789";

        UserResponseModel mockCustomerResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        User mockUser = User.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        Mockito.when(userRepository.findUserByUserId(userId)).thenReturn(mockUser);

        Mockito.when(userResponseMapper.toCustomerResponse(mockUser)).thenReturn(mockCustomerResponse);

        UserResponseModel result = customerService.getUserByUserId(userId);


        assertEquals(mockUser.getEmail(), result.getEmail());

        assertEquals(mockUser.getFirstName(), result.getFirstName());
        assertEquals(mockUser.getLastName(), result.getLastName());

        assertEquals(mockUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(mockUser.getProfilePictureUrl(), result.getProfilePictureUrl());
        assertEquals(mockUser.getUserId(), result.getUserId());

    }

    @Test
    void createCustomer() {

        String userId = "google|123456789";

        UserRequestModel mockCustomerRequest = UserRequestModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("test@email.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        User mockUser = User.builder()
                .firstName("Alice")
                .lastName("Doe")
                .profilePictureUrl("https://www.google.com")
                .email("test@email.com")
                .phoneNumber("+1234567890")
                .userId(userId)
                .build();

        UserResponseModel mockCustomerResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .profilePictureUrl("https://www.google.com")
                .email("test@email.com")
                .phoneNumber("+1234567890")
                .userId(userId)
                .build();

        Mockito.when(userRepository.existsByUserId(userId)).thenReturn(false);

        Mockito.when(userRequestMapper.toCustomer(mockCustomerRequest)).thenReturn(mockUser);

        Mockito.when(userRepository.save(mockUser)).thenReturn(mockUser);

        Mockito.when(userResponseMapper.toCustomerResponse(mockUser)).thenReturn(mockCustomerResponse);

        UserResponseModel result = customerService.addUser(mockCustomerRequest);


        assertEquals(mockUser.getEmail(), result.getEmail());

        assertEquals(mockUser.getFirstName(), result.getFirstName());
        assertEquals(mockUser.getLastName(), result.getLastName());

        assertEquals(mockUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(mockUser.getProfilePictureUrl(), result.getProfilePictureUrl());
        assertEquals(mockUser.getUserId(), result.getUserId());




    }

    @Test
    void updateCustomer() {

        String userId = "google|123456789";

        UserRequestModel mockCustomerRequest = UserRequestModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("test@email.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        User mockUserExisting = User.builder()
                .firstName("Old")
                .lastName("Doe")
                .profilePictureUrl("https://www.google.com")
                .email("new@email.com")
                .phoneNumber("+1235367890")
                .userId(userId)
                .build();

        UserResponseModel mockCustomerResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("test@email.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();



        Mockito.when(userRepository.findUserByUserId(userId)).thenReturn(mockUserExisting);

        Mockito.when(userRequestMapper.toCustomer(mockCustomerRequest)).thenReturn(mockUserExisting);

        Mockito.when(userRepository.save(mockUserExisting)).thenReturn(mockUserExisting);

        Mockito.when(userResponseMapper.toCustomerResponse(mockUserExisting)).thenReturn(mockCustomerResponse);

        UserResponseModel result = customerService.updateUser(mockCustomerRequest, userId);

        //assert
        assertEquals(mockUserExisting.getEmail(), result.getEmail());

        assertEquals(mockUserExisting.getFirstName(), result.getFirstName());
        assertEquals(mockUserExisting.getLastName(), result.getLastName());

        assertEquals(mockUserExisting.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(mockUserExisting.getProfilePictureUrl(), result.getProfilePictureUrl());
        assertEquals(mockUserExisting.getUserId(), result.getUserId());

    }

    @Test
    void updateManyFieldsForCustomer() {

        String userId = "google|123456789";

        UserRequestModel mockCustomerRequest = UserRequestModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("fresh@email.com")
                .phoneNumber("+1234569870")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        User mockUserExisting = User.builder()
                .firstName("Old")
                .lastName("Doe")
                .profilePictureUrl("https://www.google.com")
                .email("new@email.com")
                .phoneNumber("+1235367890")
                .userId(userId)
                .build();


        UserResponseModel mockCustomerResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("fresh@email.com")
                .phoneNumber("+1234569870")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        Mockito.when(userRepository.findUserByUserId(userId)).thenReturn(mockUserExisting);

        Mockito.when(userRequestMapper.toCustomer(mockCustomerRequest)).thenReturn(mockUserExisting);

        Mockito.when(userRepository.save(mockUserExisting)).thenReturn(mockUserExisting);

        Mockito.when(userResponseMapper.toCustomerResponse(mockUserExisting)).thenReturn(mockCustomerResponse);

        UserResponseModel result = customerService.updateUser(mockCustomerRequest, userId);


        assertEquals(mockUserExisting.getEmail(), result.getEmail());

        assertEquals(mockUserExisting.getFirstName(), result.getFirstName());
        assertEquals(mockUserExisting.getLastName(), result.getLastName());

        assertEquals(mockUserExisting.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(mockUserExisting.getProfilePictureUrl(), result.getProfilePictureUrl());
        assertEquals(mockUserExisting.getUserId(), result.getUserId());

    }

//    @Test
//    void deleteCustomer() {
//        // Given
//        String userId = "auth|123456789";
//        Mockito.when(userRepository.existsByUserId(userId)).thenReturn(true, false);
//        // When
//        customerService.deleteCustomer(userId);
//        // Then
//        Mockito.verify(userRepository, Mockito.times(2)).existsByUserId(userId);
//        Mockito.verify(userRepository, Mockito.times(1)).deleteCustomerByUserId(userId);
//    }



//    @Test
//    void deleteFailsForCustomer() {
//
//        String userId = "auth|123456789";
//
//        Mockito.when(userRepository.existsByUserId(userId)).thenReturn(true);
//
//        assertThrows(InvalidRequestException.class, () -> customerService.deleteCustomer(userId));
//
//    }
    @Test
    void checkIfCustomerExists() {

        String userId = "auth|123456789";

        Mockito.when(userRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = customerService.checkIfUserExistsByUserId(userId);

        assertTrue(result);
    }    @Test
    void checkIfCustomerExistsByEmail() {

        String email = "email";

        Mockito.when(userRepository.existsByUserId(email)).thenReturn(true);

        boolean result = customerService.checkIfUserExistsByUserId(email);

        assertTrue(result);
    }

    @Test
    void getCustomerByEmail() {
        String userId = "auth|123456789";
        UserResponseModel mockCustomerResponse = UserResponseModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();

        User mockUser = User.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("alice.doe@gmail.com")
                .phoneNumber("+1234567890")
                .profilePictureUrl("https://www.google.com")
                .userId(userId)
                .build();
        String email = "alice.doe@gmail.com";

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(mockUser);

        Mockito.when(userResponseMapper.toCustomerResponse(mockUser)).thenReturn(mockCustomerResponse);

        UserResponseModel result = customerService.getUserByEmail(email);
        assertEquals(mockUser.getEmail(), result.getEmail());
        assertEquals(mockUser.getFirstName(), result.getFirstName());
        assertEquals(mockUser.getLastName(), result.getLastName());
        assertEquals(mockUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(mockUser.getProfilePictureUrl(), result.getProfilePictureUrl());
        assertEquals(mockUser.getUserId(), result.getUserId());
    }

    @Test
    void getCustomerByEmailFails() {
        String email = "nonexistent.email@gmail.com";

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> customerService.getUserByEmail(email));
    }

    @Test
    void updateUserWithInvalidUserId_Fails() {
        String userId = "auth|123456789";
        UserRequestModel mockCustomerRequest = UserRequestModel.builder()
                .firstName("Alice")
                .lastName("Doe")
                .email("test@gmail.com")
                .userId("auth|123456789")
                .build();
        Mockito.when(userRepository.findUserByUserId(userId)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> customerService.updateUser(mockCustomerRequest, userId));
    }


}
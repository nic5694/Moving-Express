package com.example.backend.usersubdomain.presentationlayer;

import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.datalayer.UserRepository;
import com.example.backend.util.exceptions.UserNotFoundException;
import com.example.backend.util.exceptions.InvalidRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc//(addFilters = false)
@ActiveProfiles("test")
@AllArgsConstructor
class UserControllerIntegrationTest {
    private final String BASE_URI_USERS = "/api/v1/movingexpress/users";
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @Mock
    UserRepository testRepo;

    User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId("auth0|123456789");
        testUser.setFirstName("Alice");
        // Set other properties of the customer as needed

        testUser = userRepository.save(testUser);
    }

    @Test
    void getCustomerByUserIdWithSimpleCheck() throws Exception {
        //Arrange
        mockMvc.perform(get(BASE_URI_USERS + "?simpleCheck=true")
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(testUser.getUserId())))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerByUserIdWithSimpleCheckNotFound() throws Exception {
        mockMvc.perform(get(BASE_URI_USERS + "?simpleCheck=true")
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("oauth|notuser")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerByUserId() throws Exception {
        mockMvc.perform(get(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(testUser.getUserId())))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

//    @Test
//    @Transactional
//    void deleteCustomer() throws Exception {
//        mockMvc.perform(delete(BASE_URI_CUSTOMERS)
//                .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(testUser.getUserId())).authorities(new SimpleGrantedAuthority("ShipmentOwner")))
//                .with(csrf())
//                .accept(MediaType.APPLICATION_JSON));
//        //check if the customer is deleted
//        mockMvc.perform(get(BASE_URI_CUSTOMERS)
//                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(testUser.getUserId())))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }

    @Test
    void createCustomer() throws Exception {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("testName")
                .email("email@gmail.com")
                .profilePictureUrl("https://www.google.com")
                .userId("auth0|123456989")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String customerRequestModelJson = objectMapper.writeValueAsString(userRequestModel);

        mockMvc.perform(post(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(userRequestModel.getUserId())).authorities(new SimpleGrantedAuthority("ShipmentOwner")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerRequestModelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(userRequestModel.getFirstName()))
                .andExpect(jsonPath("$.userId").value(userRequestModel.getUserId()))
                .andExpect(jsonPath("$.email").value(userRequestModel.getEmail()))
                .andExpect(jsonPath("$.profilePictureUrl").value(userRequestModel.getProfilePictureUrl()));

        mockMvc.perform(get(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(userRequestModel.getUserId())).authorities(new SimpleGrantedAuthority("ShipmentOwner")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userRequestModel.getFirstName()))
                .andExpect(jsonPath("$.userId").value(userRequestModel.getUserId()))
                .andExpect(jsonPath("$.email").value(userRequestModel.getEmail()))
                .andExpect(jsonPath("$.profilePictureUrl").value(userRequestModel.getProfilePictureUrl()));
    }

    @Test
    void postWithInvalidBody() throws Exception {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .userId("auth0|123456989")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String customerRequestModelJson = objectMapper.writeValueAsString(userRequestModel);

        mockMvc.perform(post(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(userRequestModel.getUserId())))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerRequestModelJson))
                .andExpect(status().isBadRequest());

    }


    @Test
    void updateCustomer() throws Exception {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("testName")
                .email("email@gmail.com")
                .profilePictureUrl("https://www.google.com")
                .userId("auth0|123456989")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String customerRequestModelJson = objectMapper.writeValueAsString(userRequestModel);

        mockMvc.perform(get(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(testUser.getUserId())))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()));

        mockMvc.perform(put(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(testUser.getUserId())).authorities(new SimpleGrantedAuthority("Customer")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerRequestModelJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userRequestModel.getFirstName()))
                //userId should never change
                .andExpect(jsonPath("$.userId").value(testUser.getUserId()))
                .andExpect(jsonPath("$.email").value(userRequestModel.getEmail()))
                .andExpect(jsonPath("$.profilePictureUrl").value(userRequestModel.getProfilePictureUrl()));


        mockMvc.perform(get(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(testUser.getUserId())))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userRequestModel.getFirstName()))
                .andExpect(jsonPath("$.userId").value(testUser.getUserId()))
                .andExpect(jsonPath("$.email").value(userRequestModel.getEmail()))
                .andExpect(jsonPath("$.profilePictureUrl").value(userRequestModel.getProfilePictureUrl()));
    }


    @Test
    void updateAlmostNoInfoForCustomer() throws Exception {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("testName")
                .email("email@gmail.com")
                .profilePictureUrl("https://www.google.com")
                .userId("auth0|123456989")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String customerRequestModelJson = objectMapper.writeValueAsString(userRequestModel);
        mockMvc.perform(put(BASE_URI_USERS)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> {
                            i.subject(testUser.getUserId());

                        }).authorities(new SimpleGrantedAuthority("ShipmentOwner")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerRequestModelJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userRequestModel.getFirstName()))
                .andExpect(jsonPath("$.userId").value(testUser.getUserId()))
                .andExpect(jsonPath("$.email").value(userRequestModel.getEmail()))
                .andExpect(jsonPath("$.profilePictureUrl").value(userRequestModel.getProfilePictureUrl()));
    }

    @Test
    void getCustomerById_WithInvalidClientIdReturnCustomerNotFoundException() throws Exception {
        //Arrange
        String invalidUserId = "invalidUserId";
        //Act and Assert
        mockMvc.perform(get(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(invalidUserId)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }
    @Test
    void createCustomerClientIdThatAlreadyExistsThrowsInvalidRequestException() throws Exception {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("testName")
                .email("email@gmail.com")
                .profilePictureUrl("https://www.google.com")
                .userId("auth0|123456789")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String customerRequestModelJson = objectMapper.writeValueAsString(userRequestModel);

        mockMvc.perform(post(BASE_URI_USERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(userRequestModel.getUserId())).authorities(new SimpleGrantedAuthority("ShipmentOwner")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerRequestModelJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException));
    }
//    @Test
//    void updateCustomerWithClientIdThatDoesntExistThrowsCustomerNotFoundException() throws Exception {
//
//        String invalidUserId = "invalidUserId";
//        UserRequestModel customerRequestModel = UserRequestModel.builder()
//                .userId(invalidUserId)
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String customerRequestModelJson = objectMapper.writeValueAsString(customerRequestModel);
//        when(testRepo.findCustomerByUserId(invalidUserId)).thenReturn(null);
//        mockMvc.perform(put(BASE_URI_CUSTOMERS)
//                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject(customerRequestModel.getUserId())).authorities(new SimpleGrantedAuthority("ShipmentOwner")))
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(customerRequestModelJson))
//                .andExpect(status().isBadRequest());
//    }
}

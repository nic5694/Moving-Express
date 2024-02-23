package com.example.backend.shipmentsubdomain.presentationlayer.truck;

import com.example.backend.shipmentsubdomain.datalayer.truck.TruckRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Sql({"/data-h2.sql"})
class TruckControllerIntegrationTest {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TruckRepository truckRepository;
    private final String BASE_URI_TRUCKS = "/api/v1/movingexpress/trucks";


    @Test
    @WithMockUser(authorities = "Truck_Driver")
    public void whenGetAllTrucks_ThenReturnAllTrucks(){
        webTestClient.get().uri(BASE_URI_TRUCKS)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TruckResponseModel.class)
                .hasSize(5);
    }


}
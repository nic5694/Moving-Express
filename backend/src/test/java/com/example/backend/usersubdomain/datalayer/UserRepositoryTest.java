package com.example.backend.usersubdomain.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    String userid = "auth0|123456789";
    User user = User.builder()
            .userId("auth0|123456789")
            .firstName("John")
            .lastName("Doe")
            .email("test@gmail.com")
            .phoneNumber("1234567890")
            .build();

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
        user = userRepository.save(user);
    }
    @Test
    void getCustomerByUserId() {
        User user = userRepository.findUserByUserId(userid);
        assertEquals(userid, user.getUserId());
    }

    @Test
    void deleteCustomerByUserId() {
        userRepository.deleteCustomerByUserId(userid);
        User user = userRepository.findUserByUserId(userid);
        assertNull(user);
    }

    @Test
    void existsByUserId() {
        boolean exists = userRepository.existsByUserId(userid);
        assertTrue(exists);
    }

    @Test
    void findUserByEmail() {
        User user = userRepository.findUserByEmail("test@gmail.com");
        assertNotNull(user);
    }
}
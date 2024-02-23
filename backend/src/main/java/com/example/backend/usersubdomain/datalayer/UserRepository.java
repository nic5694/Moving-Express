package com.example.backend.usersubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUserId(String userId);
    void deleteCustomerByUserId(String userId);
    boolean existsByUserId(String userId);
    User findUserByEmail(String email);
}

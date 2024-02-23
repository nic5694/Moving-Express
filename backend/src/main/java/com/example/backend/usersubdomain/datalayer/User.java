package com.example.backend.usersubdomain.datalayer;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "Users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String userId;
    @Nullable
    private String profilePictureUrl;
    private String firstName;
    @Nullable
    private String lastName;
    private String email;
    @Nullable
    private String phoneNumber;
}

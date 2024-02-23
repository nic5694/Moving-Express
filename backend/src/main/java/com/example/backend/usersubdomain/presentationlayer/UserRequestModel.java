package com.example.backend.usersubdomain.presentationlayer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestModel {
    @NotBlank
    String userId;
    String profilePictureUrl;
    @Email
    @NotBlank
    String email;
    @NotBlank
    String firstName;
    String lastName;
    String phoneNumber;
}

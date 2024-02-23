package com.example.backend.usersubdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseModel {
    String userId;
    String profilePictureUrl;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
}

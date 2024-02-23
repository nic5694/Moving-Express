package com.example.backend.config.security.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Generated
public class UserRequestModel {
    private String username;
    private String password;
}

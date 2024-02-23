package com.example.backend.config;

import lombok.Generated;
import lombok.Value;

@Generated
@Value
public class ErrorMessge {
    private String message;

    public static ErrorMessge from(final String message) {
        return new ErrorMessge(message);
    }
}

package com.example.backend.util.exceptions;

public class InvalidPickupAndDestinationAddressException extends RuntimeException{
    public InvalidPickupAndDestinationAddressException(String message) {
        super(message);
    }
}

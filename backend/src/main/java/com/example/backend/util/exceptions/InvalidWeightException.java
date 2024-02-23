package com.example.backend.util.exceptions;

public class InvalidWeightException extends RuntimeException{
    public InvalidWeightException(String message) {
        super(message);
    }
}

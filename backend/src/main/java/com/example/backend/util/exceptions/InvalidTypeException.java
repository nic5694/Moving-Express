package com.example.backend.util.exceptions;

public class InvalidTypeException extends RuntimeException{
    public InvalidTypeException(String message) {
        super(message);
    }
}

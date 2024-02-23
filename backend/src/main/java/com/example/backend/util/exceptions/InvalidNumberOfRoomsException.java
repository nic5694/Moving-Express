package com.example.backend.util.exceptions;

public class InvalidNumberOfRoomsException extends RuntimeException{
    public InvalidNumberOfRoomsException(String message) {
        super(message);
    }
}

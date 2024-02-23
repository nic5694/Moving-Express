package com.example.backend.util.exceptions;

public class InvalidInventoryStatusException extends RuntimeException{
    public InvalidInventoryStatusException(String message) {
        super(message);
    }
}

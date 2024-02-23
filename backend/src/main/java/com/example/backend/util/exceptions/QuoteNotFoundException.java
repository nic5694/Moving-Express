package com.example.backend.util.exceptions;

public class QuoteNotFoundException extends RuntimeException{
    public QuoteNotFoundException(String message) {
        super(message);
    }
}

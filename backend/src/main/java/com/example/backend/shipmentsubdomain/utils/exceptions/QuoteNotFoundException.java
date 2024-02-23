package com.example.backend.shipmentsubdomain.utils.exceptions;

public class QuoteNotFoundException extends RuntimeException{
    public QuoteNotFoundException(String message) { super(message); }
}
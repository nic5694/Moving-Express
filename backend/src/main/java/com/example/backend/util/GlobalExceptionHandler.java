package com.example.backend.util;

import com.example.backend.util.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public HttpErrorInfo handleCustomerNotFoundException(WebRequest request, UserNotFoundException e) {
        return createHttpErrorInfo(NOT_FOUND, request, e);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(QuoteNotFoundException.class)
    public HttpErrorInfo handleNotFoundException(WebRequest request, QuoteNotFoundException ex){
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ShipmentNotFoundException.class)
    public HttpErrorInfo handleShipmentNotFoundException(WebRequest request, ShipmentNotFoundException ex){
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidOperationException.class)
    public HttpErrorInfo handleInvalidOperationException(WebRequest request, InvalidOperationException ex) {
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidRequestException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(WebRequest request, InvalidRequestException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = request.getDescription(false);
        final String message = ex.getMessage();
        return new HttpErrorInfo(httpStatus, path, message);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(InventoryNotFoundException.class)
    public HttpErrorInfo handleInventoryNotFoundException(WebRequest request, InventoryNotFoundException ex){
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ObserverCodeNotFound.class)
    public HttpErrorInfo handleObserverCodeNotFoundException(WebRequest request, ObserverCodeNotFound ex){
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidTypeException.class)
    public HttpErrorInfo handleErrorWhileCreatingItemWithInvalidType(WebRequest request, InvalidTypeException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInventoryStatusException.class)
    public HttpErrorInfo handleErrorWhileCreatingItemToInventoryWithInvalidStatus(WebRequest request, InvalidInventoryStatusException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidValueException.class)
    public HttpErrorInfo handleErrorWhileCreatingQuoteWithInvalidApproximateShipmentValue(WebRequest request, InvalidValueException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidNumberOfRoomsException.class)
    public HttpErrorInfo handleErrorWhileCreatingQuoteWithInvalidNumberOfRooms(WebRequest request, InvalidNumberOfRoomsException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidWeightException.class)
    public HttpErrorInfo handleErrorWhileCreatingQuoteWithInvalidApproximateWeight(WebRequest request, InvalidWeightException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ItemNotFoundException.class)
    public HttpErrorInfo handleItemNotFoundException(WebRequest request, ItemNotFoundException ex){
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidPickupAndDestinationAddressException.class)
    public HttpErrorInfo handleErrorWhileRequestingQuoteWithSamePickupAndDestination(WebRequest request, InvalidPickupAndDestinationAddressException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }
}
package com.example.backend.util;

import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Generated
public class HttpErrorInfo {

    private final String timestamp;
    private final String path;
    private final HttpStatus httpStatus;
    private final String message;

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Montreal"));

        this.path = path;
        timestamp = now.format(formatter);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
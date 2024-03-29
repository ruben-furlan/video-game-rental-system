package com.videogamerentalsystem.infraestucture.exception.custom;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
package com.app.freegamesapi.CustomExceptions;

// Exception thrown when the API call fails
public class ApiCallFailedException extends RuntimeException {
    public ApiCallFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
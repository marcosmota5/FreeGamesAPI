package com.app.freegamesapi.CustomExceptions;

public class ApiCallFailedException extends RuntimeException {
    public ApiCallFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
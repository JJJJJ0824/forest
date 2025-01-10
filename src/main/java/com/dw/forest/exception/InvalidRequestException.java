package com.dw.forest.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
        super();
    }
    public InvalidRequestException(String message) {
        super(message);
    }
}

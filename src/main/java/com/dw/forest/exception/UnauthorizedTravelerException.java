package com.dw.forest.exception;

public class UnauthorizedTravelerException extends RuntimeException {
    public UnauthorizedTravelerException() {super();}
    public UnauthorizedTravelerException(String message) {
        super(message);
    }
}

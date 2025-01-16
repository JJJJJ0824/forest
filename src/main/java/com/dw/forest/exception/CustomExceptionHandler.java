package com.dw.forest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>("\"error\" : \""+e.getMessage()+"\"", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException e) {
        return new ResponseEntity<>("\"error\" : \""+e.getMessage()+"\"", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedTravelerException.class)
    public ResponseEntity<String> handleUnauthorizedTravelerException(UnauthorizedTravelerException e) {
        return new ResponseEntity<>("\"error\" : \""+e.getMessage()+"\"", HttpStatus.UNAUTHORIZED);
    }
}

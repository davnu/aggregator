package com.fedex.aggregator.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity handleApiException(ApiException e) {
        return new ResponseEntity(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity handleUnexpectedException(Exception e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

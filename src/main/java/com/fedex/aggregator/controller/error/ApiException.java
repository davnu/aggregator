package com.fedex.aggregator.controller.error;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    private HttpStatus status;

    public ApiException(HttpStatus status, String message, Exception e) {
        super(message, e);
        this.status = status;
    }

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

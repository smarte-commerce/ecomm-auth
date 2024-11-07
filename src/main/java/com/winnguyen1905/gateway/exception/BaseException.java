package com.winnguyen1905.gateway.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {

    private HttpStatus status;
    private ErrorResponse errorResponse;
    private String[] args;

    protected BaseException(HttpStatus status,ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.status = status;
        this.errorResponse = errorResponse;
    }

}

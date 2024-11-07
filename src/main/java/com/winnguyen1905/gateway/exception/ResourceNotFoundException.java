package com.winnguyen1905.gateway.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
  public ResourceNotFoundException(HttpStatus status, ErrorResponse errorResponse) {
    super(status, errorResponse);
  }
}
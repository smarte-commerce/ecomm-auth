package com.winnguyen1905.gateway.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
  public ResourceAlreadyExistsException(String message) {
      super(message);
  }
}

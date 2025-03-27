package com.winnguyen1905.auth.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
  public ResourceAlreadyExistsException(String message) {
      super(message);
  }
}

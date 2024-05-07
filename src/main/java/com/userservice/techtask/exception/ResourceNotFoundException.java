package com.userservice.techtask.exception;

/**
 * Exception thrown when child object are not found.
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message + " is not found.");
  }
}

package com.userservice.techtask.exception;

/**
 * Exception thrown when a user is found to be under the minimum age requirement.
 */
public class UnderAgeException extends RuntimeException {

  public UnderAgeException(String message) {
    super(message);
  }

}

package com.userservice.techtask.exception;

/**
 * Custom exception indicating an invalid date argument.
 */
public class DateArgumentException extends RuntimeException {

  public DateArgumentException(String massage) {
    super(massage);
  }

}

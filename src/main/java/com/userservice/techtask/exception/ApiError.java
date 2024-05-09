package com.userservice.techtask.exception;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents an API error response.
 */
@Data
public class ApiError {

  private LocalDateTime time;
  private int status;
  private String message;
  private String details;

  /**
   * Constructs an API error.
   *
   * @param time    The time when the error occurred.
   * @param status  The HTTP status code.
   * @param message The error message.
   * @param details Additional details about the error.
   */
  public ApiError(LocalDateTime time, int status, String message, String details) {
    super();
    this.time = time;
    this.status = status;
    this.message = message;
    this.details = details;
  }

}
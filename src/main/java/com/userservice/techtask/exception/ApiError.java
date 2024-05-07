package com.userservice.techtask.exception;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiError {

  private LocalDateTime time;
  private int status;
  private String message;
  private String details;

  public ApiError(LocalDateTime time, int status, String message, String details) {
    super();
    this.time = time;
    this.status = status;
    this.message = message;
    this.details = details;
  }

}
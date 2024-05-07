package com.userservice.techtask.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class User {

  @Email(message = "Invalid email address")
  @Size(max = 100)
  @NotBlank(message = "Mail name is required")
  private String email;

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @Past(message = "Birth date must be in the past")
  private LocalDate birthDate;

  @Size(max = 255, message = "Address must be at most 255 characters long")
  private String address;

  @Size(max = 20, message = "Phone number must be at most 20 characters long")
  private String phoneNumber;

}

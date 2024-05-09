package com.userservice.techtask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object (DTO) for updating user information.
 */
@Getter
@Setter
@EqualsAndHashCode
public class UserUpdateDto {

  @Email(message = "Invalid email address")
  @Size(max = 100)
  private String email;

  private String firstName;

  private String lastName;

  @Past(message = "Birth date must be in the past")
  private LocalDate birthDate;

  @Size(max = 255, message = "Address must be at most 255 characters long")
  private String address;

  @Size(max = 20, message = "Phone number must be at most 20 characters long")
  private String phoneNumber;

}

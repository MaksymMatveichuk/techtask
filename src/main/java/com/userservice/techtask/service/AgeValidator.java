package com.userservice.techtask.service;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Validator class to check if a user is of adult age based on their birth date.
 */
@Component
public class AgeValidator {

  @Value("${user.minAge}")
  private int minAge;

  /**
   * Checks if a user is of adult age based on their birth date.
   *
   * @param birthDate The birth date of the user
   * @return True if the user is of adult age, false otherwise
   */
  public boolean ageIsAdult(LocalDate birthDate) {
    int age = Period.between(birthDate, LocalDate.now()).getYears();
    return age >= minAge;
  }

}

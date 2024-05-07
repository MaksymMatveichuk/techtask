package com.userservice.techtask.service;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AgeValidator {

  @Value("${user.minAge}")
  private int minAge;

  public boolean ageIsAdult(LocalDate birthDate) {
    int age = Period.between(birthDate, LocalDate.now()).getYears();
    return age >= minAge;
  }

}

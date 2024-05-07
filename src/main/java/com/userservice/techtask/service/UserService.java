package com.userservice.techtask.service;

import com.userservice.techtask.entity.User;
import com.userservice.techtask.exception.DateArgumentException;
import com.userservice.techtask.exception.ResourceNotFoundException;
import com.userservice.techtask.exception.UnderAgeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final Map<String, User> users = new HashMap<>();
  private final AgeValidator ageValidator;

  public User createUser(User user) {
    if (ageValidator.ageIsAdult(user.getBirthDate())) {
      users.put(user.getEmail(), user);
      return user;
    } else {
      throw new UnderAgeException("User is under the minimum age requirement");
    }
  }

  public User getUserByEmail(String email) {
    if (users.containsKey(email)) {
      return users.get(email);
    } else {
      throw new ResourceNotFoundException(email);
    }
  }

  public List<User> getAllUsers() {
    if (users.isEmpty()) {
      throw new ResourceNotFoundException("Any user");
    } else {
      return new ArrayList<>(users.values());
    }
  }

  public User updateUser(String email, User updatedUser) {
    if (users.containsKey(email)) {
      return users.put(email, updatedUser);
    } else {
      throw new ResourceNotFoundException(email);
    }
  }

  public User updateUserByField(String email, User updatedUser) {
    if (!users.containsKey(email)) {
      throw new ResourceNotFoundException(email);
    }
    User existingUser = users.get(email);
    if (updatedUser.getEmail() != null) {
      existingUser.setEmail(updatedUser.getEmail());
    }
    if (updatedUser.getFirstName() != null) {
      existingUser.setFirstName(updatedUser.getFirstName());
    }
    if (updatedUser.getLastName() != null) {
      existingUser.setLastName(updatedUser.getLastName());
    }
    if (updatedUser.getBirthDate() != null) {
      existingUser.setBirthDate(updatedUser.getBirthDate());
    }
    if (updatedUser.getPhoneNumber() != null) {
      existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
    }
    if (updatedUser.getAddress() != null) {
      existingUser.setAddress(updatedUser.getAddress());
    }
    users.put(email, existingUser);

    return existingUser;
  }

  public void deleteUser(String email) {
    if (users.remove(email) == null) {
      throw new ResourceNotFoundException(email);
    }
  }

  public List<User> getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
    if (fromDate.isAfter(toDate)) {
      throw new DateArgumentException("'From' date should be before 'To' date");
    }
    return users.values().stream()
        .filter(user -> !user.getBirthDate().isBefore(fromDate)
            && !user.getBirthDate().isAfter(toDate))
        .collect(Collectors.toList());
  }
}

package com.userservice.techtask.service;

import com.userservice.techtask.dto.UserUpdateDto;
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

/**
 * Service class for performing operations related to users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final Map<String, User> users = new HashMap<>();
  private final AgeValidator ageValidator;

  /**
   * Creates a new user.
   *
   * @param user The user to be created.
   * @return The created user.
   * @throws UnderAgeException If the user is under the minimum age requirement.
   */
  public User createUser(User user) {
    if (ageValidator.ageIsAdult(user.getBirthDate())) {
      users.put(user.getEmail(), user);
      return user;
    } else {
      throw new UnderAgeException("User is under the minimum age requirement");
    }
  }

  /**
   * Retrieves a user by email.
   *
   * @param email The email of the user to retrieve.
   * @return The user with the specified email.
   * @throws ResourceNotFoundException If the user with the specified email does not exist.
   */
  public User getUserByEmail(String email) {
    if (users.containsKey(email)) {
      return users.get(email);
    } else {
      throw new ResourceNotFoundException(email);
    }
  }

  /**
   * Retrieves all users.
   *
   * @return A list of all users.
   * @throws ResourceNotFoundException If there are no users.
   */
  public List<User> getAllUsers() {
    if (users.isEmpty()) {
      throw new ResourceNotFoundException("Any user not found!");
    } else {
      return new ArrayList<>(users.values());
    }
  }

  /**
   * Updates a user's information.
   *
   * @param email       The email of the user to update.
   * @param updatedUser The updated user data.
   * @return The updated user.
   * @throws ResourceNotFoundException If the user with the specified email does not exist.
   */
  public User updateUser(String email, User updatedUser) {
    if (users.containsKey(email)) {
      users.remove(email);
      users.put(updatedUser.getEmail(), updatedUser);
      return updatedUser;
    } else {
      throw new ResourceNotFoundException(email);
    }
  }

  /**
   * Updates specific fields of a user by email.
   *
   * @param email       The email of the user to update.
   * @param updatedUser The updated user data.
   * @return The updated user.
   * @throws ResourceNotFoundException If the user with the specified email does not exist.
   */
  public User updateUserByField(String email, UserUpdateDto updatedUser) {
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

  /**
   * Deletes a user by email.
   *
   * @param email The email of the user to delete.
   * @throws ResourceNotFoundException If the user with the specified email does not exist.
   */
  public void deleteUser(String email) {
    if (users.remove(email) == null) {
      throw new ResourceNotFoundException(email);
    }
  }

  /**
   * Retrieves users within a specified birth date range.
   *
   * @param fromDate The start date of the birth date range.
   * @param toDate   The end date of the birth date range.
   * @return A list of users within the specified birth date range.
   * @throws DateArgumentException If the 'from' date is after the 'to' date.
   */
  public List<User> getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
    if (fromDate.isAfter(toDate)) {
      throw new DateArgumentException("'From' date should be before 'To' date.");
    }
    return users.values().stream()
        .filter(user -> !user.getBirthDate().isBefore(fromDate)
            && !user.getBirthDate().isAfter(toDate))
        .collect(Collectors.toList());
  }
}

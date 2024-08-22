package com.userservice.techtask.controller;

import com.userservice.techtask.dto.UserUpdateDto;
import com.userservice.techtask.entity.User;
import com.userservice.techtask.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class that handles user-related HTTP requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/health")
  public ResponseEntity<String> healthCheck() {
    return new ResponseEntity<>("Healthy new", HttpStatus.OK);
  }

  /**
   * Endpoint for creating a new user.
   *
   * @param user The user to be created.
   * @return The created user.
   */
  @PostMapping
  public User createUser(@RequestBody @Valid User user) {
    return userService.createUser(user);
  }

  /**
   * Endpoint for retrieving a user by email.
   *
   * @param email The email of the user to retrieve.
   * @return The user with the specified email.
   */
  @GetMapping("/{email}")
  public User getUserByEmail(@PathVariable String email) {
    return userService.getUserByEmail(email);
  }

  /**
   * Endpoint for retrieving all users.
   *
   * @return A list of all users.
   */
  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  /**
   * Endpoint for updating a user by email.
   *
   * @param email       The email of the user to update.
   * @param updatedUser The updated user data.
   * @return The updated user.
   */
  @PutMapping("/{email}")
  public User updateUser(@PathVariable String email, @Valid @RequestBody User updatedUser) {
    return userService.updateUser(email, updatedUser);
  }

  /**
   * Endpoint for updating specific fields of a user by email.
   *
   * @param email       The email of the user to update.
   * @param updatedUser The updated user data.
   * @return The updated user.
   */
  @PutMapping("/{email}/update-fields")
  public User updateUserByField(@PathVariable String email,
      @Valid @RequestBody UserUpdateDto updatedUser) {
    return userService.updateUserByField(email, updatedUser);
  }

  /**
   * Endpoint for deleting a user by email.
   *
   * @param email The email of the user to delete.
   */
  @DeleteMapping("/{email}")
  public void deleteUser(@PathVariable String email) {
    userService.deleteUser(email);
  }

  /**
   * Endpoint for searching users within a birth date range.
   *
   * @param fromDate The start date of the birth date range.
   * @param toDate   The end date of the birth date range.
   * @return A list of users within the specified birth date range.
   */
  @GetMapping("/search")
  public List<User> getUsersByBirthDateRange(
      @RequestParam("from") @Valid LocalDate fromDate,
      @RequestParam("to") @Valid LocalDate toDate) {
    return userService.getUsersByBirthDateRange(fromDate, toDate);
  }
}

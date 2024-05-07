package com.userservice.techtask.controller;

import com.userservice.techtask.entity.User;
import com.userservice.techtask.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {


  private final UserService userService;


  @PostMapping
  public User createUser(@RequestBody @Valid User user) {
    return userService.createUser(user);
  }

  @GetMapping("/{email}")
  public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
    User user = userService.getUserByEmail(email);
    if (user != null) {
      return ResponseEntity.ok(user);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @PutMapping("/{email}")
  public User updateUser(@PathVariable String email, @Valid @RequestBody User updatedUser) {
    return userService.updateUser(email, updatedUser);
  }

  @DeleteMapping("/{email}")
  public void deleteUser(@PathVariable String email) {
    userService.deleteUser(email);
  }

  @GetMapping("/search")
  public List<User> getUsersByBirthDateRange(
      @RequestParam("from") @Valid LocalDate fromDate,
      @RequestParam("to") @Valid LocalDate toDate) {
    return userService.getUsersByBirthDateRange(fromDate, toDate);
  }

}

package com.userservice.techtask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.userservice.techtask.dto.UserUpdateDto;
import com.userservice.techtask.entity.User;
import com.userservice.techtask.exception.DateArgumentException;
import com.userservice.techtask.exception.ResourceNotFoundException;
import com.userservice.techtask.exception.UnderAgeException;
import com.userservice.techtask.service.AgeValidator;
import com.userservice.techtask.service.UserService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlainUnitServiceTest {

  @Mock
  private AgeValidator ageValidator;
  @InjectMocks
  private UserService userService;
  private User goodUser;
  private User changedUser;
  private Map<String, User> userList;


  @BeforeEach
  public void setUp() {

    goodUser = new User();
    goodUser.setEmail("test@example.com");
    goodUser.setFirstName("John");
    goodUser.setLastName("Doe");
    goodUser.setBirthDate(LocalDate.of(1990, 1, 1));
    goodUser.setAddress("123 Main St, City");
    goodUser.setPhoneNumber("1234567890");

    changedUser = new User();
    changedUser.setEmail("test2@example.com");
    changedUser.setFirstName("John1");
    changedUser.setLastName("Doe");
    changedUser.setBirthDate(LocalDate.of(2002, 1, 1));
    changedUser.setAddress("123 Main St, City");
    changedUser.setPhoneNumber("0987654321");

    userList = new HashMap<>();
    userList.put("test@example.com", goodUser);
    userList.put("test1@example.com", changedUser);
  }

  @Test
  public void createUserWhenAdultTest() {
    when(ageValidator.ageIsAdult(goodUser.getBirthDate())).thenReturn(true);

    User createdUser = userService.createUser(goodUser);

    assertEquals(goodUser.getEmail(), createdUser.getEmail());
    verify(ageValidator).ageIsAdult(goodUser.getBirthDate());
  }

  @Test
  public void createUserThrowExceptionWhenUnderAgeTest() {
    when(ageValidator.ageIsAdult(goodUser.getBirthDate())).thenReturn(false);
    assertThrows(UnderAgeException.class, () -> userService.createUser(goodUser));
  }

  @Test
  public void getUserByEmailWithExistingEmailTest() {
    when(ageValidator.ageIsAdult(goodUser.getBirthDate())).thenReturn(true);

    userService.createUser(goodUser);
    User retrievedUser = userService.getUserByEmail("test@example.com");

    assertEquals(goodUser, retrievedUser);
  }

  @Test
  public void getUserByEmailWithNonExistingEmailTest() {
    assertThrows(ResourceNotFoundException.class,
        () -> userService.getUserByEmail("nonexistent@example.com"));
  }

  @Test
  public void getAllUsersWithNonEmptyUserListShouldReturnAllUsers() {
    when(ageValidator.ageIsAdult(any())).thenReturn(true);
    userService.createUser(goodUser);
    userService.createUser(changedUser);
    List<User> allUsers = userService.getAllUsers();

    assertEquals(2, allUsers.size());
    assertTrue(allUsers.contains(goodUser));
    assertTrue(allUsers.contains(changedUser));
  }

  @Test
  public void getAllUsersWithEmptyUserListShouldThrowResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class, userService::getAllUsers);
  }

  @Test
  public void updateUserWithExistingEmailShouldReturnUpdatedUser() {
    User newUser = new User();
    newUser.setEmail("test3@example.com");
    newUser.setFirstName("John1");
    newUser.setLastName("Doe");
    newUser.setBirthDate(LocalDate.of(2002, 1, 1));
    newUser.setAddress("123 Main St, City");
    newUser.setPhoneNumber("0987654321");
    when(ageValidator.ageIsAdult(any())).thenReturn(true);

    userService.createUser(goodUser);
    userService.createUser(newUser);
    User returnedUser = userService.updateUser("test@example.com", changedUser);

    assertEquals(changedUser, returnedUser);
    assertEquals("John1", userService.getUserByEmail("test2@example.com").getFirstName());
  }

  @Test
  public void updateUserWithNonExistingEmailShouldThrowResourceNotFoundException() {
    String email = "nonexistent@example.com";
    assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(email, changedUser));
  }

  @Test
  public void updateUserByField_ShouldUpdateExistingUser() {
    UserUpdateDto updatedUserDto = new UserUpdateDto();
    updatedUserDto.setFirstName("Jane");
    when(ageValidator.ageIsAdult(any(LocalDate.class))).thenReturn(true);

    userService.createUser(goodUser);
    User updatedUser = userService.updateUserByField(goodUser.getEmail(), updatedUserDto);

    assertEquals("Jane", updatedUser.getFirstName());
    assertEquals(goodUser.getLastName(), updatedUser.getLastName());
  }

  @Test
  public void updateUserByFieldWithNonExistingEmailShouldThrowResourceNotFoundException() {
    String email = "nonexistent@example.com";
    UserUpdateDto updatedUserDto = new UserUpdateDto();

    assertThrows(ResourceNotFoundException.class,
        () -> userService.updateUserByField(email, updatedUserDto));
  }

  @Test
  public void deleteUserWithExistingEmailShouldRemoveUser() {
    when(ageValidator.ageIsAdult(any(LocalDate.class))).thenReturn(true);

    userService.createUser(goodUser);
    userService.createUser(changedUser);
    userService.deleteUser(goodUser.getEmail());

    assertThrows(ResourceNotFoundException.class,
        () -> userService.getUserByEmail(goodUser.getEmail()));
  }

  @Test
  public void deleteUserWithNonExistingEmailShouldThrowResourceNotFoundException() {
    String email = "nonexistent@example.com";

    assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(email));
  }


  @Test
  public void getUsersByBirthDateRangeWithValidRangeShouldReturnFilteredUsers() {
    LocalDate fromDate = LocalDate.of(1990, 1, 1);
    LocalDate toDate = LocalDate.of(2000, 12, 31);
    when(ageValidator.ageIsAdult(any(LocalDate.class))).thenReturn(true);

    userService.createUser(goodUser);
    userService.createUser(changedUser);

    List<User> filteredUsers = userService.getUsersByBirthDateRange(fromDate, toDate);

    assertEquals(1, filteredUsers.size());
    assertEquals(goodUser, filteredUsers.get(0));
  }

  @Test
  public void getUsersByBirthDateRangeWithInvalidRangeShouldThrowDateArgumentException() {
    LocalDate fromDate = LocalDate.of(2000, 1, 1);
    LocalDate toDate = LocalDate.of(1990, 12, 31);

    assertThrows(
        DateArgumentException.class, () -> userService.getUsersByBirthDateRange(fromDate, toDate));
  }

}

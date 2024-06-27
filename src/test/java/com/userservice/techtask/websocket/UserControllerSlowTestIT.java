package com.userservice.techtask.websocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.techtask.controller.UserController;
import com.userservice.techtask.entity.User;
import com.userservice.techtask.exception.ResourceNotFoundException;
import com.userservice.techtask.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerSlowTestIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private UserService userService;
  @InjectMocks
  private UserController userController;
  private User goodUser;
  private User changedUser;
  private User badUser;
  private List<User> userList;

  @BeforeEach
  public void setUp() {
    goodUser = new User();
    goodUser.setEmail("test@example.com");
    goodUser.setFirstName("John");
    goodUser.setLastName("Doe");
    goodUser.setBirthDate(LocalDate.of(1990, 1, 1));
    goodUser.setAddress("123 Main St, City");
    goodUser.setPhoneNumber("1234567890");

    badUser = new User();
    badUser.setEmail("invalid-email");
    badUser.setFirstName("");
    badUser.setLastName("");
    badUser.setBirthDate(LocalDate.now().plusDays(1));
    badUser.setAddress("a");
    badUser.setPhoneNumber("123456789012345678901");

    changedUser = new User();
    changedUser.setEmail("test1@example.com");
    changedUser.setFirstName("John");
    changedUser.setLastName("Doe");
    changedUser.setBirthDate(LocalDate.of(2002, 1, 1));
    changedUser.setAddress("123 Main St, City");
    changedUser.setPhoneNumber("0987654321");

    userList = new ArrayList<>();
    userList.add(goodUser);
    userList.add(changedUser);
  }

  @Test
  public void createUserTest() throws Exception {
    when(userService.createUser(goodUser)).thenReturn(goodUser);
    String responseAsString = mockMvc
        .perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(goodUser)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    User resultUser = objectMapper.readValue(responseAsString, User.class);
    assertEquals(goodUser, resultUser);
  }

  @Test
  public void updateUserTest() throws Exception {
    String requestBody = objectMapper.writeValueAsString(changedUser);
    when(userService.updateUser(any(), any())).thenReturn(changedUser);
    String responseAsString = mockMvc
        .perform(put("/users/{email}", "test1@example.com")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    User resultUser = objectMapper.readValue(responseAsString, User.class);
    assertEquals(changedUser, resultUser);
  }

  @Test
  public void updateUserByFieldTest() throws Exception {
    String requestBody = objectMapper.writeValueAsString(changedUser);
    when(userService.updateUserByField(any(), any())).thenReturn(changedUser);
    String responseAsString = mockMvc
        .perform(put("/users/{email}/update-fields", "test1@example.com")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    User resultUser = objectMapper.readValue(responseAsString, User.class);
    assertEquals(changedUser, resultUser);
  }

  @Test
  public void createUserValidationTest() throws Exception {
    when(userService.createUser(badUser)).thenThrow(ResourceNotFoundException.class);
    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(badUser)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").exists());

    assertThrows(ResourceNotFoundException.class, () -> {
      userService.createUser(badUser);
    });
  }

  @Test
  public void invalidEndpointsTest() throws Exception {
    mockMvc.perform(get("/invalidEndpoint"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("No static resource invalidEndpoint."))
        .andExpect(jsonPath("$.status")
            .value(404))
        .andExpect(jsonPath("$.details")
            .value("Please check your request`s resource."));

    mockMvc.perform(post("/invalidEndpoint"))
        .andExpect(status().isNotFound());
    mockMvc.perform(put("/invalidEndpoint"))
        .andExpect(status().isNotFound());
    mockMvc.perform(delete("/invalidEndpoint"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getUserByEmailTest() throws Exception {
    when(userService.getUserByEmail(anyString())).thenReturn(goodUser);

    String responseAsString = mockMvc.perform(get("/users/{email}",
            "test@example.com"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse()
        .getContentAsString();

    User resultUserDto = objectMapper.readValue(responseAsString, User.class);
    assertEquals(goodUser, resultUserDto);

  }

  @Test
  public void getAllUsersTest() throws Exception {
    when(userService.getAllUsers()).thenReturn(userList);
    String responseAsString = mockMvc
        .perform(get("/users"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse()
        .getContentAsString();

    List<User> actualDtoList = objectMapper.readValue(responseAsString,
        new TypeReference<List<User>>() {
        });
    assertEquals(userList, actualDtoList);
  }

  @Test
  public void getUsersByBirthDateRangeTest() throws Exception {
    // Заглушка для вашего сервиса
    when(userService.getUsersByBirthDateRange(any(), any())).thenReturn(userList);
    String responseAsString = mockMvc
        .perform(get("/users/search")
            .param("from", "1989-01-01")
            .param("to", "2013-12-31"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse()
        .getContentAsString();
    List<User> actualDtoList = objectMapper.readValue(responseAsString,
        new TypeReference<List<User>>() {
        });
    assertEquals(userList, actualDtoList);
  }

  @Test
  void deleteTest() throws Exception {
    mockMvc.perform(delete("/users/{email}", "test@example.com"))
        .andExpect(status().isOk());
  }

}
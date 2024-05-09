# User Management RESTful API

This application implements a RESTful API based on a Spring Boot web application, with controllers responsible for managing resources named Users. Initially, a Map is used for saving users, but it is recommended to use PostgreSQL and Spring Data JPA or JpaRepository for better performance and scalability.

## Features

- Allows CRUD operations for managing user data.
- Supports JSON format for API responses.
- Provides endpoints for creating, updating, and searching users.

## Endpoints

### Get User
- **Method:** GET
- **Endpoint:** `/users"`
- **Example Request:**
  http://localhost:8080/users

### Get User by email
- **Method:** GET
- **Endpoint:** `/users/{email}"`
- **Example Request:**
  http://localhost:8080/users/{email}

### Create User
- **Method:** POST
- **Endpoint:** `/users`
- **Request Body:**
    ```json
    {
        "email": "alice@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "birthDate": "1999-05-08",
        "address": "123 Street",
        "phoneNumber": "1234567890"
    }
    ```

### Update User
- **Method:** PUT
- **Endpoint:** `/users/{email}` or `/users/{email}/update-fields`
- **Request Body:**
    ```json
    {
        "email": "alice1@example.com",
        "firstName": "John1",
        "lastName": "Doe1",
        "birthDate": "1995-05-09",
        "address": "333 Street",
        "phoneNumber": "1234567890"
    }
    ```
  or
    ```json
    {
        "email": "alice1@example.com",
        "phoneNumber": "111111111"
    }
    ```
- **Alternate Endpoint for Partial Update:**
  `/users/{email}/update-fields`

### Delete User
- **Method:** DELETE
- **Endpoint:** `/users/{email}"`
- **Example Request:**
  http://localhost:8080/users/alice77@example.com
 

### Search Users by Birth Date Range
- **Method:** GET
- **Endpoint:** `/users/search`
- **Query Parameters:**
    - `from`: Start date of birthdate range (e.g., `1994-01-08`)
    - `to`: End date of birthdate range (e.g., `2005-05-08`)
- **Example Request:**
  `http://localhost:8080/users/search?from=1994-01-08&to=2005-05-08`


## Technologies Used

- Spring Boot
- Hibernate
- JSON
- REST
- Mock
- JUnit

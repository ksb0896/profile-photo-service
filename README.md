# Profile Photo Service

A microservice for managing user profile photos with upload, download, update, and delete capabilities. This service is built with Spring Boot and uses MySQL for data persistence.

## Project Overview

This microservice is part of a larger user service ecosystem and provides RESTful APIs for managing profile photos for users across different banks. It supports file upload/download operations with comprehensive error handling and validation.

## Technology Stack

- **Java Version**: 17
- **Framework**: Spring Boot 3.5.6
- **Database**: MySQL 8.0+
- **Build Tool**: Maven
- **Key Dependencies**:
  - Spring Boot Data JPA
  - Spring Boot Web (REST APIs)
  - MySQL Connector/J
  - Lombok (for reducing boilerplate)
  - Spring Boot DevTools (for development)

## Server Configuration

- **Local Server Port**: 8082
- **Base Path**: `/v1/banks/{bankId}/users/{userId}`

## Database Configuration

- **Driver**: com.mysql.cj.jdbc.Driver
- **Default Database**: `user_profile_db`
- **Default User**: root
- **Default Password**: mysql
- **Hibernate DDL Auto**: none

## File Upload Configuration

- **Max File Size**: 10MB
- **Max Request Size**: 10MB

## API Endpoints

### 1. Get Profile Photo
```
GET /v1/banks/{bankId}/users/{userId}/photo
```
- **Description**: Retrieve a user's profile photo
- **Path Parameters**:
  - `bankId`: Bank identifier
  - `userId`: User identifier
- **Response**: Image file (byte array) with appropriate content type
- **Status Codes**:
  - 200 OK - Photo retrieved successfully
  - 404 Not Found - Photo does not exist

### 2. Upload Profile Photo
```
POST /v1/banks/{bankId}/users/{userId}/photo
```
- **Description**: Upload a new profile photo for a user
- **Path Parameters**:
  - `bankId`: Bank identifier
  - `userId`: User identifier
- **Request**: Multipart form data with `file` parameter
- **Response**: Success message
- **Status Codes**:
  - 201 Created - Photo uploaded successfully
  - 409 Conflict - Photo already exists for this user

### 3. Update Profile Photo
```
PUT /v1/banks/{bankId}/users/{userId}/photo
```
- **Description**: Update an existing profile photo
- **Path Parameters**:
  - `bankId`: Bank identifier
  - `userId`: User identifier
- **Request**: Multipart form data with `file` parameter
- **Response**: Success message
- **Status Codes**:
  - 200 OK - Photo updated successfully
  - 404 Not Found - Photo does not exist

### 4. Delete Profile Photo
```
DELETE /v1/banks/{bankId}/users/{userId}/photo
```
- **Description**: Delete a user's profile photo
- **Path Parameters**:
  - `bankId`: Bank identifier
  - `userId`: User identifier
- **Response**: No content
- **Status Codes**:
  - 204 No Content - Photo deleted successfully
  - 404 Not Found - Photo does not exist

## Project Structure

```
src/
├── main/
│   ├── java/com/ksb/micro/profile_photo/
│   │   ├── ProfilePhotoApplication.java       (Entry point)
│   │   ├── controller/
│   │   │   └── ProfilePhotoController.java    (REST endpoints)
│   │   ├── model/
│   │   │   └── ProfilePhoto.java              (Entity model)
│   │   ├── repository/
│   │   │   └── ProfilePhotoRepository.java    (Data access layer)
│   │   └── service/
│   │       └── impl/
│   │           ├── ProfilePhotoService.java   (Service interface)
│   │           └── ProfilePhotoServiceImpl.java (Service implementation)
│   └── resources/
│       └── application.properties              (Configuration)
└── test/
    └── java/com/ksb/micro/profile_photo/
        └── ProfilePhotoApplicationTests.java  (Unit tests)
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Installation & Setup

1. Clone or navigate to the project directory
2. Create the MySQL database:
   ```sql
   CREATE DATABASE user_profile_db;
   ```

3. Update database credentials in `src/main/resources/application.properties` if needed:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. Build the project:
   ```bash
   mvn clean build
   ```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The service will start on `http://localhost:8082`

## Logging

- **Spring Web**: DEBUG level
- **Apache Catalina**: DEBUG level

## Future Enhancements

- Eureka Service Discovery integration (commented out, ready to uncomment)
- Caching mechanisms

## Notes

- The service currently runs independently; Eureka client integration is available but disabled
- All endpoints use bank-scoped and user-scoped parameters for multi-tenancy support
- Maximum file size is limited to 10MB for security and performance reasons


# Task Management System - Backend

Secure Java Spring Boot backend for a Task Management System.

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Web
- Spring Security
- JWT (JJWT)
- BCrypt
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Lombok

## Architecture

Controller -> Service -> Repository -> MySQL

Security flow:

Frontend -> JWT Bearer Token -> JwtAuthenticationFilter -> Spring Security -> Controller

## Features

- User registration
- User login
- BCrypt password hashing
- JWT authentication
- USER / ADMIN roles
- User-owned task isolation
- Task CRUD
- Task status and priority
- Dashboard task counts
- DTO-based API responses
- Bean Validation
- Global exception handling
- CORS configuration

## MySQL Setup

Create the database:

```sql
CREATE DATABASE task_management;
```

The application creates/updates tables using Hibernate.

Default development credentials in `application.properties` are:

```text
username=root
password=root
```

Better: set an environment variable:

```text
DB_PASSWORD=your_mysql_password
```

## Run

Make sure MySQL is running, then:

```bash
mvn clean spring-boot:run
```

Application:

```text
http://localhost:8080
```

## API

### Register

POST `/api/auth/register`

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "Password@123"
}
```

### Login

POST `/api/auth/login`

```json
{
  "email": "john@example.com",
  "password": "Password@123"
}
```

The response contains a JWT.

For protected endpoints send:

```text
Authorization: Bearer <token>
```

### Tasks

GET `/api/tasks`

POST `/api/tasks`

```json
{
  "title": "Build dashboard",
  "description": "Create the task dashboard UI",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2026-10-01"
}
```

PUT `/api/tasks/{id}`

DELETE `/api/tasks/{id}`

GET `/api/tasks/dashboard`

### Current User

GET `/api/auth/me`

### Admin

GET `/api/admin/users`

This requires an ADMIN role.

## Important Security Notes

- Passwords are never stored as plain text.
- BCrypt is used for password hashing.
- JWT is used for stateless authentication.
- Users can access only their own tasks.
- Admin endpoints require `ROLE_ADMIN`.
- Never commit production JWT secrets or database passwords to Git.
- For deployment, use environment variables/secrets.
- HTTPS should be used in production.

## Project Flow

1. User registers.
2. Password is encoded using BCrypt.
3. User is stored in MySQL.
4. User logs in.
5. Spring Security authenticates the credentials.
6. Backend creates a JWT.
7. Frontend stores the token and sends it as a Bearer token.
8. JWT filter validates the token on protected requests.
9. Controller receives an authenticated principal.
10. Service uses the authenticated email to access only that user's tasks.
11. JPA/Hibernate performs database operations.
12. DTO is returned as JSON.

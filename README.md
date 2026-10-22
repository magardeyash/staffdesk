# StaffDesk

A web-based Employee Management System built with Java 21 and Spring Boot 3. The application provides full CRUD functionality for managing employee records, with server-side rendering via Thymeleaf, persistent storage in MySQL, and validated form handling.

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 21 LTS |
| Framework | Spring Boot 3.4.3 |
| Web Layer | Spring MVC, Embedded Apache Tomcat |
| Template Engine | Thymeleaf 3.1 |
| ORM | Spring Data JPA, Hibernate 6.6 |
| Database | MySQL 8.0+ (Connector/J 9.1.0) |
| Validation | Jakarta Bean Validation (Hibernate Validator) |
| Frontend | Bootstrap 5.3 |
| Testing | JUnit 5, Mockito, AssertJ, Spring MockMvc |
| Build | Apache Maven 3.9+ |

---

## Features

- Create, view, edit, and delete employee records
- Search employees by name (case-insensitive, partial match)
- Server-side form validation with inline field-level error messages
- Unique email enforcement with custom uniqueness check
- Flash messages for operation feedback (success and error)
- Graceful handling of invalid or missing resource IDs via a custom exception handler
- Delete confirmation via browser dialog
- Responsive layout suitable for desktop and mobile
- Sample data seeded automatically on first startup

---

## Architecture

The application follows a standard four-layer architecture with single-responsibility separation:

```
HTTP Request
    |
    v
EmployeeController       -- Request routing, model population, view resolution
    |
    v
EmployeeService          -- Business rules, transaction boundaries, validation logic
    |
    v
EmployeeRepository       -- Spring Data JPA queries, database access
    |
    v
MySQL (staffdesk_db)     -- Persistent storage
```

Key design decisions:
- Constructor injection used throughout; no field-level `@Autowired`
- Post-Redirect-Get (PRG) pattern applied to all form submissions to prevent duplicate submissions on refresh
- `RedirectAttributes.addFlashAttribute` used for cross-redirect messaging
- `@ExceptionHandler` in the controller handles `ResourceNotFoundException` without exposing stack traces

---

## Database Schema

The `employees` table is generated automatically by Hibernate on first startup (`ddl-auto=update`).

```sql
CREATE TABLE employees (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(120) NOT NULL UNIQUE,
    department VARCHAR(50)  NOT NULL,
    salary     DOUBLE       NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
```

---

## Prerequisites

- Java 21 SDK
- Apache Maven 3.8 or later
- MySQL Server running on port 3306

---

## Application Routes

| Method | Route | Description |
|---|---|---|
| GET | `/employees` | List all employees |
| GET | `/employees?keyword={name}` | Search by name |
| GET | `/employees/new` | Display create form |
| POST | `/employees/save` | Create or update an employee |
| GET | `/employees/{id}/edit` | Display pre-filled edit form |
| GET | `/employees/{id}/delete` | Delete an employee |

---

## Running Tests

Run the full test suite:

```bash
mvn test
```

The test suite includes 21 tests across three test classes:

| Test Class | Type | Count | What It Covers |
|---|---|---|---|
| `EmployeeServiceTest` | Unit (Mockito) | 11 | CRUD methods, custom exception, search, email uniqueness |
| `EmployeeControllerTest` | MVC Slice (MockMvc) | 8 | HTTP routing, validation errors, redirect behavior, flash attributes |
| `StaffDeskApplicationTests` | Integration | 2 | Application context load, end-to-end database roundtrip |

---

## Project Structure

```
src/
  main/
    java/com/staffdesk/
      StaffDeskApplication.java          # Application entry point
      config/
        DataSeeder.java                  # Loads sample records on empty database
      controller/
        EmployeeController.java          # Handles all web requests
      entity/
        Employee.java                    # JPA entity mapped to employees table
      exception/
        ResourceNotFoundException.java   # Thrown when a requested ID does not exist
      repository/
        EmployeeRepository.java          # Spring Data JPA repository interface
      service/
        EmployeeService.java             # Business logic and transaction management
    resources/
      application.properties            # Configuration
      templates/
        employees.html                  # Employee list and search view
        employee-form.html              # Create and edit form view
  test/
    java/com/staffdesk/
      StaffDeskApplicationTests.java
      controller/EmployeeControllerTest.java
      service/EmployeeServiceTest.java
```

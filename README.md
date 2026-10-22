# StaffDesk — Employee Management System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-green.svg)](https://www.thymeleaf.org/)
[![MySQL](https://img.shields.io/badge/MySQL-26.7-blue.svg)](https://www.mysql.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)](https://getbootstrap.com/)
[![Build Status](https://img.shields.io/badge/Tests-19%20Passing-success.svg)]()

StaffDesk is a full-stack Employee Management Web Application built with **Java 21**, **Spring Boot 3**, **Thymeleaf**, **Spring Data JPA (Hibernate)**, and **MySQL**. It implements standard enterprise software patterns including layered architecture, server-side rendering, bean validation, and the Post-Redirect-Get (PRG) pattern.

---

## 📸 Key Features

- **Employee Directory**: Clean responsive interface built with Bootstrap 5 displaying ID, Name, Email, Department, and formatted Salary.
- **Full CRUD Operations**:
  - **Create**: Add new staff members with client and server-side field validation.
  - **Read**: Browse all employees or filter instantly by name.
  - **Update**: Edit existing employee details pre-populated in form fields.
  - **Delete**: Remove employee records with JavaScript confirmation dialog.
- **Search Capability**: Case-insensitive partial name search query powered by Spring Data JPA derived queries.
- **Robust Validation**: Enforced via Jakarta Bean Validation (`@NotBlank`, `@Email`, `@DecimalMin`, `@Size`, and custom unique email validation).
- **Graceful Error Handling**: Custom `ResourceNotFoundException` with `@ExceptionHandler` to redirect missing IDs cleanly without revealing stack traces.
- **User Feedback**: Dynamic Bootstrap flash alert messages for successful operations and validation warnings.
- **Comprehensive Testing Suite**: Unit tests using Mockito and MVC slice tests using MockMvc covering services and web controllers.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Language** | Java 21 LTS |
| **Framework** | Spring Boot 3.4.3 |
| **Web / MVC** | Spring MVC & Embedded Apache Tomcat |
| **Template Engine** | Thymeleaf 3.1 |
| **ORM / Persistence** | Spring Data JPA & Hibernate 6.6 |
| **Database** | MySQL Server 8.0+ / 9.x+ (Connector/J 9.1.0) |
| **Validation** | Jakarta Bean Validation (Hibernate Validator) |
| **Frontend Styling** | Bootstrap 5.3 & Bootstrap Icons |
| **Testing** | JUnit 5, Mockito, AssertJ, Spring Boot Test (MockMvc) |
| **Build Tool** | Apache Maven 3.9+ |

---

## 🏛️ Architecture Overview

The application follows a clean, single-responsibility **Layered Architecture**:

```
Browser / Client (HTTP)
       │
       ▼
[ Controller Layer ]  ──> EmployeeController (Request routing, view resolution, PRG pattern)
       │
       ▼
 [ Service Layer ]    ──> EmployeeService (Business rules, transaction boundaries, validation checks)
       │
       ▼
[ Repository Layer ]  ──> EmployeeRepository (Spring Data JPA queries, CRUD execution)
       │
       ▼
 [ Database Layer ]   ──> MySQL Server (Persistent storage: 'employees' table)
```

---

## 🗄️ Database Schema

The database table is managed automatically by Hibernate (`ddl-auto=update`):

```sql
CREATE TABLE employees (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    salary DOUBLE NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
```

---

## 🚀 Getting Started

### Prerequisites

1. **Java 21 SDK** installed and configured (`java -version`).
2. **Apache Maven 3.8+** installed (`mvn -version`).
3. **MySQL Server** installed and running on port `3306`.

### 1. Database Setup

Open MySQL CLI or MySQL Workbench and run:

```sql
CREATE DATABASE IF NOT EXISTS staffdesk_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configure Credentials

Verify credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/staffdesk_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the Application

Run using Maven from the project root:

```powershell
mvn spring-boot:run
```

Or build the standalone executable JAR and run it:

```powershell
mvn clean package
java -jar target/staffdesk-0.0.1-SNAPSHOT.jar
```

The application will start on **`http://localhost:8080`**.

---

## 🌐 Application URL Routes

| HTTP Method | Route | Description |
|---|---|---|
| `GET` | `/` or `/employees` | Display all employees or search results |
| `GET` | `/employees?keyword=alice` | Search employees by name |
| `GET` | `/employees/new` | Render blank employee creation form |
| `POST` | `/employees/save` | Validate and save new or edited employee |
| `GET` | `/employees/{id}/edit` | Render edit form pre-filled with employee details |
| `GET` | `/employees/{id}/delete` | Delete employee record and redirect |

---

## 🧪 Running Automated Tests

Run the full test suite with Maven:

```powershell
mvn test
```

### Test Coverage Highlights:
- **`EmployeeServiceTest`**: 11 unit tests using Mockito verifying retrieval, custom exceptions, save, delete, search, and email uniqueness logic.
- **`EmployeeControllerTest`**: 8 slice tests using `@WebMvcTest` and `MockMvc` verifying request routing, HTTP status codes, flash attributes, model attributes, and validation error binding.
- **`StaffDeskApplicationTests`**: End-to-end integration test validating context loading and real database roundtrips.

---

## 📄 Resume Bullet Points

- *Engineered a responsive Employee Management Web Application using **Java 21**, **Spring Boot 3**, **Spring Data JPA**, and **MySQL**, adhering to enterprise layered architecture and constructor dependency injection.*
- *Implemented server-side rendering with **Thymeleaf** and **Bootstrap 5**, utilizing the **Post-Redirect-Get (PRG)** pattern and flash attributes to prevent duplicate form submissions and provide seamless UI feedback.*
- *Enforced data integrity with **Jakarta Bean Validation** (`@NotBlank`, `@Email`, `@DecimalMin`), custom case-insensitive uniqueness checks, and centralized exception handling for missing resources.*
- *Authored 19+ automated unit and slice tests using **JUnit 5**, **Mockito**, and **MockMvc**, ensuring robust service business logic and HTTP controller validation coverage.*

---

## 💡 Top 10 Technical Interview Questions & Answers

<details>
<summary><strong>1. Why did you use Constructor Injection instead of @Autowired on fields?</strong></summary>
<p>
Constructor injection makes dependencies explicit, guarantees that an object cannot be created in an uninitialized state, facilitates immutability (by marking fields <code>final</code>), and makes unit testing straightforward without needing reflection or a Spring container.
</p>
</details>

<details>
<summary><strong>2. What is the Post-Redirect-Get (PRG) pattern and why is it important?</strong></summary>
<p>
PRG is a web development design pattern where a POST request (such as submitting a form) returns a redirect response (HTTP 302/303) rather than directly rendering the view. This prevents duplicate submissions if the user refreshes the page or presses the back button.
</p>
</details>

<details>
<summary><strong>3. How does Spring Data JPA generate SQL from method names like <code>findByNameContainingIgnoreCase</code>?</strong></summary>
<p>
Spring Data JPA uses the query derivation mechanism. It parses the method name tokens (<code>findBy</code>, <code>Name</code>, <code>Containing</code>, <code>IgnoreCase</code>) and generates an Abstract Syntax Tree (AST) which Hibernate translates into an SQL <code>SELECT ... WHERE LOWER(name) LIKE LOWER('%...%')</code> statement at startup.
</p>
</details>

<details>
<summary><strong>4. What is the difference between @Controller and @RestController?</strong></summary>
<p>
<code>@Controller</code> is used for traditional Spring MVC applications where handler methods return view names (like <code>"employees"</code>) resolved to HTML templates by a view resolver (Thymeleaf). <code>@RestController</code> is a convenience combination of <code>@Controller</code> and <code>@ResponseBody</code> where methods return domain objects serialized directly into JSON/XML for RESTful APIs.
</p>
</details>

<details>
<summary><strong>5. How does Jakarta Bean Validation work in the controller layer?</strong></summary>
<p>
The <code>@Valid</code> annotation triggers Spring's validator to check constraints (e.g. <code>@NotBlank</code>, <code>@Email</code>) on the incoming form object. The results are injected into the adjacent <code>BindingResult</code> parameter. If errors are present, the controller bypasses database operations and re-renders the form with field-specific errors.
</p>
</details>

<details>
<summary><strong>6. How does Thymeleaf perform two-way data binding with Spring MVC models?</strong></summary>
<p>
Thymeleaf integrates with Spring MVC through attributes like <code>th:object</code> (binds the form to a model attribute) and <code>th:field</code> (binds input fields to object properties, generating matching <code>id</code>, <code>name</code>, and current <code>value</code> attributes while automatically reading validation errors).
</p>
</details>

<details>
<summary><strong>7. What role does Hibernate play in Spring Data JPA?</strong></summary>
<p>
JPA (Jakarta Persistence API) is a specification defining interfaces and annotations for Object-Relational Mapping (ORM). Hibernate is the actual underlying implementation provider that handles SQL generation, dirty checking, caching, and entity state management.
</p>
</details>

<details>
<summary><strong>8. What does <code>spring.jpa.hibernate.ddl-auto=update</code> do, and why should it not be used in production?</strong></summary>
<p>
<code>update</code> instructs Hibernate to inspect the entity mappings and automatically alter the database schema (adding tables or columns) without destroying existing data. In production, schema changes should be managed deterministically using database migration tools like Flyway or Liquibase to prevent accidental data modification or table locks.
</p>
</details>

<details>
<summary><strong>9. How does MockMvc test controllers without deploying to a real web server?</strong></summary>
<p>
<code>MockMvc</code> simulates the Spring MVC execution chain (DispatcherServlet, interceptors, message converters, controllers) in-memory using mock HTTP request and response objects. This enables fast, reliable testing of routing and validation without the overhead of starting an embedded Tomcat server.
</p>
</details>

<details>
<summary><strong>10. Why did you use <code>RedirectAttributes.addFlashAttribute()</code> instead of standard Model attributes?</strong></summary>
<p>
Standard model attributes are passed in the HTTP request scope and are lost across HTTP redirects. Flash attributes are temporarily stored in the HTTP session before the redirect and automatically removed once rendered on the target page, making them ideal for success/error alerts after a PRG redirection.
</p>
</details>

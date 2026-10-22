package com.staffdesk;

import com.staffdesk.entity.Employee;
import com.staffdesk.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end context and database integration test.
 * Connects to the local MySQL instance and verifies schema creation and persistence.
 */
@SpringBootTest
class StaffDeskApplicationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Application context loads and connects to MySQL")
    void contextLoads() {
        assertThat(employeeRepository).isNotNull();
    }

    @Test
    @DisplayName("End-to-End MySQL persistence test: create, read, update, delete")
    void testDatabasePersistenceFlow() {
        // 1. Create
        Employee emp = new Employee("Integration Test User", "integration.test@example.com", "QA", 72000.0);
        Employee saved = employeeRepository.save(emp);
        assertThat(saved.getId()).isNotNull();

        // 2. Read
        Optional<Employee> found = employeeRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Integration Test User");

        // 3. Search
        List<Employee> searchResults = employeeRepository.findByNameContainingIgnoreCase("integration test");
        assertThat(searchResults).isNotEmpty();

        // 4. Update
        found.get().setSalary(78000.0);
        Employee updated = employeeRepository.save(found.get());
        assertThat(updated.getSalary()).isEqualTo(78000.0);

        // 5. Delete
        employeeRepository.deleteById(saved.getId());
        assertThat(employeeRepository.findById(saved.getId())).isEmpty();
    }
}

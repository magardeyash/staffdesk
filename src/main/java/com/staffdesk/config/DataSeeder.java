package com.staffdesk.config;

import com.staffdesk.entity.Employee;
import com.staffdesk.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Automatically seeds initial demo employee records on startup if the database is empty.
 * Implemented as a separate @Component so it won't interfere with @WebMvcTest slices.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public DataSeeder(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            employeeRepository.saveAll(List.of(
                    new Employee("Alex Morgan", "alex.morgan@staffdesk.com", "Engineering", 92000.0),
                    new Employee("Elena Rostova", "elena.rostova@staffdesk.com", "Human Resources", 68000.0),
                    new Employee("Marcus Chen", "marcus.chen@staffdesk.com", "Product", 88000.0),
                    new Employee("Sarah Jenkins", "sarah.jenkins@staffdesk.com", "Finance", 75000.0),
                    new Employee("David Kim", "david.kim@staffdesk.com", "Marketing", 64000.0)
            ));
        }
    }
}

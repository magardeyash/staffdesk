package com.staffdesk;

import com.staffdesk.entity.Employee;
import com.staffdesk.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Main entry point for the StaffDesk application.
 *
 * @SpringBootApplication enables:
 * 1. @Configuration: Tags the class as a source of bean definitions.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to configure beans based on classpath dependencies.
 * 3. @ComponentScan: Scans for beans in com.staffdesk and all sub-packages.
 */
@SpringBootApplication
public class StaffDeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffDeskApplication.class, args);
    }

    /**
     * Optional sample data seeder:
     * Populates initial employees if database is currently empty.
     */
    @Bean
    public CommandLineRunner loadSampleData(EmployeeRepository employeeRepository) {
        return args -> {
            if (employeeRepository.count() == 0) {
                employeeRepository.saveAll(List.of(
                        new Employee("Alex Morgan", "alex.morgan@staffdesk.com", "Engineering", 92000.0),
                        new Employee("Elena Rostova", "elena.rostova@staffdesk.com", "Human Resources", 68000.0),
                        new Employee("Marcus Chen", "marcus.chen@staffdesk.com", "Product", 88000.0),
                        new Employee("Sarah Jenkins", "sarah.jenkins@staffdesk.com", "Finance", 75000.0),
                        new Employee("David Kim", "david.kim@staffdesk.com", "Marketing", 64000.0)
                ));
            }
        };
    }
}

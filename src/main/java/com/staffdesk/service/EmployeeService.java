package com.staffdesk.service;

import com.staffdesk.entity.Employee;
import com.staffdesk.exception.ResourceNotFoundException;
import com.staffdesk.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer containing business logic for Employee management.
 * Follows Spring's best practice of constructor injection.
 */
@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Constructor Injection (Spring automatically injects the repository bean)
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Retrieve all employees.
     */
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Retrieve an employee by ID.
     * Throws ResourceNotFoundException if the ID does not exist.
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    /**
     * Save a new employee or update an existing one.
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Delete an employee by ID.
     * Verifies existence first to fail gracefully if the employee does not exist.
     */
    public void deleteEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete: Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    /**
     * Search employees by name keyword (case-insensitive).
     * If the keyword is blank or null, returns all employees.
     */
    @Transactional(readOnly = true)
    public List<Employee> searchEmployeesByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findByNameContainingIgnoreCase(keyword.trim());
    }

    /**
     * Check if an email is already in use by another employee.
     * Useful for checking duplicate email violations during create / edit.
     */
    @Transactional(readOnly = true)
    public boolean isEmailUnique(String email, Long currentId) {
        Optional<Employee> existing = employeeRepository.findByEmailIgnoreCase(email);
        if (existing.isEmpty()) {
            return true;
        }
        // If currentId is provided (edit mode), allow if it belongs to the same employee
        return currentId != null && existing.get().getId().equals(currentId);
    }
}

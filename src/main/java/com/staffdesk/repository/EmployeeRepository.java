package com.staffdesk.repository;

import com.staffdesk.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Employee entity.
 * Provides out-of-the-box CRUD methods (save, findById, findAll, deleteById, count).
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Spring Data derived query method:
     * Generates: SELECT * FROM employees WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))
     */
    List<Employee> findByNameContainingIgnoreCase(String name);

    /**
     * Find an employee by email (case-insensitive).
     */
    Optional<Employee> findByEmailIgnoreCase(String email);
}

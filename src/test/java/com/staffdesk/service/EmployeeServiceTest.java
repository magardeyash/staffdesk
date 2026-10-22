package com.staffdesk.service;

import com.staffdesk.entity.Employee;
import com.staffdesk.exception.ResourceNotFoundException;
import com.staffdesk.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for EmployeeService using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee(1L, "Alice Johnson", "alice@example.com", "Engineering", 85000.0);
    }

    @Test
    @DisplayName("getAllEmployees should return full list of employees")
    void getAllEmployees_ShouldReturnList() {
        Employee emp2 = new Employee(2L, "Bob Smith", "bob@example.com", "HR", 60000.0);
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(sampleEmployee, emp2));

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Alice Johnson");
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getEmployeeById should return employee when ID exists")
    void getEmployeeById_WhenFound_ShouldReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        Employee result = employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getEmployeeById should throw ResourceNotFoundException when ID does not exist")
    void getEmployeeById_WhenNotFound_ShouldThrowException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found with id: 99");

        verify(employeeRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("saveEmployee should persist and return saved employee")
    void saveEmployee_ShouldReturnSavedEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        Employee saved = employeeService.saveEmployee(sampleEmployee);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);
        verify(employeeRepository, times(1)).save(sampleEmployee);
    }

    @Test
    @DisplayName("deleteEmployeeById should delete when ID exists")
    void deleteEmployeeById_WhenExists_ShouldDelete() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployeeById(1L);

        verify(employeeRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteEmployeeById should throw ResourceNotFoundException when ID does not exist")
    void deleteEmployeeById_WhenNotExists_ShouldThrowException() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployeeById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cannot delete: Employee not found with id: 99");

        verify(employeeRepository, times(1)).existsById(99L);
        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("searchEmployeesByName should delegate to findByNameContainingIgnoreCase when keyword provided")
    void searchEmployeesByName_WhenKeywordProvided_ShouldCallRepo() {
        when(employeeRepository.findByNameContainingIgnoreCase("alice"))
                .thenReturn(List.of(sampleEmployee));

        List<Employee> result = employeeService.searchEmployeesByName("alice");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice Johnson");
        verify(employeeRepository, times(1)).findByNameContainingIgnoreCase("alice");
    }

    @Test
    @DisplayName("searchEmployeesByName should return all employees when keyword is null or blank")
    void searchEmployeesByName_WhenBlank_ShouldReturnAll() {
        when(employeeRepository.findAll()).thenReturn(List.of(sampleEmployee));

        List<Employee> resultNull = employeeService.searchEmployeesByName(null);
        List<Employee> resultBlank = employeeService.searchEmployeesByName("   ");

        assertThat(resultNull).hasSize(1);
        assertThat(resultBlank).hasSize(1);
        verify(employeeRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("isEmailUnique should return true when email does not exist in DB")
    void isEmailUnique_WhenNotExists_ShouldReturnTrue() {
        when(employeeRepository.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.empty());

        boolean isUnique = employeeService.isEmailUnique("new@example.com", null);

        assertThat(isUnique).isTrue();
    }

    @Test
    @DisplayName("isEmailUnique should return true when email belongs to same employee (edit mode)")
    void isEmailUnique_WhenBelongsToSameEmployee_ShouldReturnTrue() {
        when(employeeRepository.findByEmailIgnoreCase("alice@example.com")).thenReturn(Optional.of(sampleEmployee));

        boolean isUnique = employeeService.isEmailUnique("alice@example.com", 1L);

        assertThat(isUnique).isTrue();
    }

    @Test
    @DisplayName("isEmailUnique should return false when email belongs to another employee")
    void isEmailUnique_WhenBelongsToDifferentEmployee_ShouldReturnFalse() {
        when(employeeRepository.findByEmailIgnoreCase("alice@example.com")).thenReturn(Optional.of(sampleEmployee));

        boolean isUnique = employeeService.isEmailUnique("alice@example.com", 2L);

        assertThat(isUnique).isFalse();
    }
}

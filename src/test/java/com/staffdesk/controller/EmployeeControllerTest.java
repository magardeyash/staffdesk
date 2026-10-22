package com.staffdesk.controller;

import com.staffdesk.entity.Employee;
import com.staffdesk.exception.ResourceNotFoundException;
import com.staffdesk.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Controller slice test using MockMvc to test HTTP interactions,
 * model attributes, view names, validation, and error handling.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("GET /employees should return employees view and populate model")
    void testListEmployees() throws Exception {
        Employee emp = new Employee(1L, "Alice Johnson", "alice@example.com", "Engineering", 80000.0);
        when(employeeService.searchEmployeesByName(null)).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attributeExists("employees"));

        verify(employeeService).searchEmployeesByName(null);
    }

    @Test
    @DisplayName("GET /employees/new should return employee-form view with empty employee")
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/employees/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("pageTitle", "Add New Employee"));
    }

    @Test
    @DisplayName("POST /employees/save with valid data should save employee and redirect")
    void testSaveEmployee_Valid() throws Exception {
        when(employeeService.isEmailUnique("charlie@example.com", null)).thenReturn(true);

        mockMvc.perform(post("/employees/save")
                        .param("name", "Charlie Brown")
                        .param("email", "charlie@example.com")
                        .param("department", "Finance")
                        .param("salary", "65000.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("successMessage", "Employee added successfully!"));

        verify(employeeService).saveEmployee(any(Employee.class));
    }

    @Test
    @DisplayName("POST /employees/save with invalid data should return form with validation errors")
    void testSaveEmployee_Invalid() throws Exception {
        // Missing name, invalid email, negative salary
        mockMvc.perform(post("/employees/save")
                        .param("name", "")
                        .param("email", "not-an-email")
                        .param("department", "")
                        .param("salary", "-500.00"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().hasErrors());

        verify(employeeService, never()).saveEmployee(any());
    }

    @Test
    @DisplayName("POST /employees/save with duplicate email should reject and return form")
    void testSaveEmployee_DuplicateEmail() throws Exception {
        when(employeeService.isEmailUnique("alice@example.com", null)).thenReturn(false);

        mockMvc.perform(post("/employees/save")
                        .param("name", "Alice Clone")
                        .param("email", "alice@example.com")
                        .param("department", "Engineering")
                        .param("salary", "70000.00"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().attributeHasFieldErrorCode("employee", "email", "duplicate.email"));

        verify(employeeService, never()).saveEmployee(any());
    }

    @Test
    @DisplayName("GET /employees/{id}/edit should populate model and return employee-form")
    void testShowEditForm_Found() throws Exception {
        Employee emp = new Employee(1L, "Alice Johnson", "alice@example.com", "Engineering", 80000.0);
        when(employeeService.getEmployeeById(1L)).thenReturn(emp);

        mockMvc.perform(get("/employees/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().attribute("employee", hasProperty("name", is("Alice Johnson"))));
    }

    @Test
    @DisplayName("GET /employees/{id}/edit when ID not found should redirect with error message")
    void testShowEditForm_NotFound() throws Exception {
        when(employeeService.getEmployeeById(999L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 999"));

        mockMvc.perform(get("/employees/999/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("errorMessage", containsString("Employee not found with id: 999")));
    }

    @Test
    @DisplayName("GET /employees/{id}/delete should delete employee and redirect")
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(get("/employees/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("successMessage", "Employee deleted successfully!"));

        verify(employeeService).deleteEmployeeById(1L);
    }
}

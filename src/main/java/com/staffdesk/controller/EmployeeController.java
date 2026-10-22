package com.staffdesk.controller;

import com.staffdesk.entity.Employee;
import com.staffdesk.exception.ResourceNotFoundException;
import com.staffdesk.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping({"/", "/employees"})
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping({"", "/"})
    public String listEmployees(
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {
        List<Employee> employees = employeeService.searchEmployeesByName(keyword);
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        return "employees";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("pageTitle", "Add New Employee");
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (employee.getEmail() != null && !employee.getEmail().isBlank()) {
            if (!employeeService.isEmailUnique(employee.getEmail(), employee.getId())) {
                bindingResult.rejectValue("email", "duplicate.email", "This email address is already registered.");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", employee.getId() == null ? "Add New Employee" : "Edit Employee");
            return "employee-form";
        }

        boolean isNew = (employee.getId() == null);
        employeeService.saveEmployee(employee);

        String message = isNew ? "Employee added successfully!" : "Employee updated successfully!";
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("pageTitle", "Edit Employee (ID: " + id + ")");
        return "employee-form";
    }

    @GetMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployeeById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        return "redirect:/employees";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/employees";
    }
}

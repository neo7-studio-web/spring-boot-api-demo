package com.openclassrooms.api.controller;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.model.Employee;
import com.openclassrooms.api.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  /**
   * Create - Add a new employee
   * 
   * @param employee An object employee
   * @return The employee object saved
   */
  @PostMapping("/")
  public Employee createEmployee(@Valid @RequestBody Employee employee) {
    return employeeService.saveEmployee(employee);
  }

  /**
   * Read - Get one employee
   * 
   * @param id The id of the employee
   * @return An Employee object full filled
   */
  @GetMapping("/{id}")
  public Employee getEmployee(@PathVariable("id") final Long id) {
    Optional<Employee> employee = employeeService.getEmployee(id);
    return employee.orElseThrow(() -> new NoSuchElementException("No employee found with this id: " + id));
  }

  /**
   * Read - Get all employees
   * 
   * @return - An Iterable object of Employee full filled
   */
  @GetMapping("/all")
  public Iterable<Employee> getEmployees() {
    return employeeService.getEmployees();
  }

  /**
   * Update - Update an existing employee
   * 
   * @param id              - The id of the employee to update
   * @param updatedEmployee - The employee object updated
   * @return
   */
  @PutMapping("/{id}")
  public Employee updateEmployee(@PathVariable("id") final Long id, @Valid @RequestBody Employee updatedEmployee) {
    Optional<Employee> e = employeeService.getEmployee(id);
    Employee currentEmployee = e
        .orElseThrow(() -> new NoSuchElementException("Cannot edit employee. No employee found with this id: " + id));
    return employeeService.saveEmployee(employeeService.copyEmployee(updatedEmployee, currentEmployee));
  }

  /**
   * Delete - Delete an employee
   * 
   * @param id - The id of the employee to delete
   */
  @DeleteMapping("/{id}")
  public void deleteEmployee(@PathVariable("id") final Long id) {
    Optional<Employee> e = employeeService.getEmployee(id);
    e.orElseThrow(() -> new NoSuchElementException("Cannot delete employee. No employee found with this id: " + id));
    employeeService.deleteEmployee(id);
  }

}

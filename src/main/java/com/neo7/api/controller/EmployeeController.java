package com.neo7.api.controller;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neo7.api.model.Employee;
import com.neo7.api.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  Logger logger = LoggerFactory.getLogger(EmployeeController.class);

  /**
   * Create - Add a new employee
   * 
   * @param employee An object employee
   * @return The employee object saved
   */
  @PostMapping("")
  public Employee createEmployee(@Valid @RequestBody Employee employee) {
    logger.trace("Creating employee: " + employee);
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
    logger.trace("Retrieving employee with id: " + id);
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
    logger.trace("Retrieving all employees");
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
    logger.trace("Updating employee with id: " + id);
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
    logger.trace("Deleting employee with id: " + id);
    Optional<Employee> e = employeeService.getEmployee(id);
    e.orElseThrow(() -> new NoSuchElementException("Cannot delete employee. No employee found with this id: " + id));
    employeeService.deleteEmployee(id);
  }

}

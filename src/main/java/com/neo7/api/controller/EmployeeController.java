package com.neo7.api.controller;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
  public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
    logger.trace("Creating employee: " + employee);
    Employee createdEmployee = employeeService.saveEmployee(employee);
    if (Objects.isNull(createdEmployee)) {
      return ResponseEntity.noContent().build(); // 204
    }
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdEmployee.getId())
        .toUri();
    logger.trace("Employee created. URI=" + location);
    return ResponseEntity.created(location).build(); // 201 with created entity location
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
   * @param id               - The id of the employee to update
   * @param employeeToUpdate - The employee object updated
   * @return
   */
  @PutMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(@PathVariable("id") final Long id,
      @Valid @RequestBody Employee employeeToUpdate) {
    logger.trace("Updating employee with id: " + id);
    Optional<Employee> e = employeeService.getEmployee(id);
    Employee currentEmployee = e
        .orElseThrow(() -> new NoSuchElementException("Cannot update employee. No employee found with this id: " + id));
    Employee updatedEmployee = employeeService
        .saveEmployee(employeeService.copyEmployee(employeeToUpdate, currentEmployee));
    if (Objects.isNull(updatedEmployee)) {
      return ResponseEntity.noContent().build(); // 204
    }
    return ResponseEntity.ok(updatedEmployee); // 200
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

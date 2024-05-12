package com.openclassrooms.api.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.openclassrooms.api.model.Employee;
import com.openclassrooms.api.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    ResponseEntity.ok(employee);
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
   * @param id       - The id of the employee to update
   * @param employee - The employee object updated
   * @return
   */
  @PutMapping("/{id}")
  public Employee updateEmployee(@PathVariable("id") final Long id, @Valid @RequestBody Employee employee) {
    Optional<Employee> e = employeeService.getEmployee(id);
    Employee currentEmployee = e
        .orElseThrow(() -> new NoSuchElementException("Cannot edit employee. No employee found with this id: " + id));

    currentEmployee.setFirstName(employee.getFirstName());
    currentEmployee.setLastName(employee.getLastName());
    currentEmployee.setMail(employee.getMail());
    currentEmployee.setPassword(employee.getPassword());
    return employeeService.saveEmployee(currentEmployee);
  }

  /**
   * Delete - Delete an employee
   * 
   * @param id - The id of the employee to delete
   */
  @DeleteMapping("/{id}")
  public void deleteEmployee(@PathVariable("id") final Long id) {
    employeeService.deleteEmployee(id);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

    Gson gson = new Gson();
    Map<String, Object> message = new LinkedHashMap<>();
    message.put("message", "Validation failed");

    List<String> details = new ArrayList<>();
    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }
    message.put("details", details);

    return new ResponseEntity<String>(gson.toJson(message), HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<?> handleNotFoundExceptions(NoSuchElementException ex) {

    Gson gson = new Gson();
    Map<String, Object> message = new LinkedHashMap<>();
    message.put("message", "Entity not found");
    message.put("details", ex.getMessage());

    return new ResponseEntity<String>(gson.toJson(message), HttpStatus.NOT_FOUND);
  }

}

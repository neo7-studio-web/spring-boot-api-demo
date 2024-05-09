package com.openclassrooms.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    if (employee.isPresent()) {
      return employee.get();
    } else {
      return null;
    }
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
  public Employee updateEmployee(@PathVariable("id") final Long id, @RequestBody Employee employee) {
    Optional<Employee> e = employeeService.getEmployee(id);
    if (e.isPresent()) {
      Employee currentEmployee = e.get();

      String firstName = employee.getFirstName();
      if (firstName != null) {
        currentEmployee.setFirstName(firstName);
      }
      String lastName = employee.getLastName();
      if (lastName != null) {
        currentEmployee.setLastName(lastName);
        ;
      }
      String mail = employee.getMail();
      if (mail != null) {
        currentEmployee.setMail(mail);
      }
      String password = employee.getPassword();
      if (password != null) {
        currentEmployee.setPassword(password);
        ;
      }
      employeeService.saveEmployee(currentEmployee);
      return currentEmployee;
    } else {
      return null;
    }
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

    JSONObject jo = new JSONObject();
    jo.put("message", "Validation failed");

    // ALL ERRORS
    List<String> details = new ArrayList<>();
    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }

    // FIRST ERROR ONLY (but random order ....)
    // List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    // String details = errors.get(0).getDefaultMessage();

    jo.put("details", details);

    return new ResponseEntity(jo.toString(), HttpStatus.BAD_REQUEST);
  }

}

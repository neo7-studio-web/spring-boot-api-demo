package com.neo7.api.service;

import java.util.Optional;

import com.neo7.api.model.Employee;

public interface EmployeeService {

  public Optional<Employee> getEmployee(final Long id);

  public Iterable<Employee> getEmployees();

  public void deleteEmployee(final Long id);

  public Employee saveEmployee(Employee employee);

  public Employee copyEmployee(Employee fromEmployee, Employee toEmployee);
}

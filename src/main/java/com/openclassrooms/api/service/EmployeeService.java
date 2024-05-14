package com.openclassrooms.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.model.Employee;
import com.openclassrooms.api.model.Ward;
import com.openclassrooms.api.repository.EmployeeRepository;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  public Optional<Employee> getEmployee(final Long id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isPresent()) { // Remove cascade link
      employee.get().getWards().forEach(w -> w.setEmployee(null));
    }
    return employee;
  }

  public Iterable<Employee> getEmployees() {
    return employeeRepository.findAll();
  }

  public void deleteEmployee(final Long id) {
    employeeRepository.deleteById(id);
  }

  public Employee saveEmployee(Employee employee) {
    for (Ward w : employee.getWards()) {
      w.setEmployee(employee);
    }
    return employeeRepository.save(employee);
  }

  public Employee copyEmployee(Employee fromEmployee, Employee toEmployee) {
    toEmployee.setFirstName(fromEmployee.getFirstName());
    toEmployee.setLastName(fromEmployee.getLastName());
    toEmployee.setMail(fromEmployee.getMail());
    toEmployee.setPassword(fromEmployee.getPassword());
    toEmployee.setWards(fromEmployee.getWards());
    return toEmployee;
  }
}

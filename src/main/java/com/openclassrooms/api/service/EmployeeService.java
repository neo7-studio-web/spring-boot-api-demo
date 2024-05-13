package com.openclassrooms.api.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.model.Employee;
import com.openclassrooms.api.model.Ward;
import com.openclassrooms.api.repository.EmployeeRepository;
import com.openclassrooms.api.repository.WardRepository;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private WardRepository wardRepository;

  public Optional<Employee> getEmployee(final Long id) {
    return employeeRepository.findById(id);
  }

  public Iterable<Employee> getEmployees() {
    return employeeRepository.findAll();
  }

  public void deleteEmployee(final Long id) {
    employeeRepository.deleteById(id);
  }

  public Employee saveEmployee(Employee employee) {
    Set<Ward> savedWards = new HashSet<>();
    for (Ward w : employee.getWards()) {
      Ward savedWard = wardRepository.save(w);
      savedWards.add(savedWard);
    }
    employee.setWards(savedWards);
    Employee savedEmployee = employeeRepository.save(employee);
    return savedEmployee;
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

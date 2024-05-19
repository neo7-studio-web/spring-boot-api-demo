package com.neo7.api.service.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neo7.api.model.Employee;
import com.neo7.api.model.Ward;
import com.neo7.api.repository.EmployeeRepository;
import com.neo7.api.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

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

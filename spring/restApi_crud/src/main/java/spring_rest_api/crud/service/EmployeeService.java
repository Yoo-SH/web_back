package spring_rest_api.crud.service;

import spring_rest_api.crud.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();
}

package spring_rest_api.crud.service;

import org.springframework.stereotype.Service;
import spring_rest_api.crud.dao.EmployeeDAO;
import spring_rest_api.crud.entity.Employee;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDAO employeeDAO;

    public EmployeeServiceImpl(EmployeeDAO theEmployeeDAO) {
        employeeDAO = theEmployeeDAO;
    }


    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }
}

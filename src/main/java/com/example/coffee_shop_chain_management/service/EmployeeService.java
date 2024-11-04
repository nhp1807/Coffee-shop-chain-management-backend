package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateEmployeeDTO;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BranchRepository branchRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(CreateEmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setDob(employeeDTO.getDob());
        employee.setPhone(employeeDTO.getPhone());
        employee.setEmail(employeeDTO.getEmail());
        employee.setAddress(employeeDTO.getAddress());
        employee.setChatID(employeeDTO.getChatID());

        Branch branch = branchRepository.findById(employeeDTO.getBranchID()).orElseThrow(() -> new RuntimeException("Branch not found"));
        employee.setBranch(branch);

        return employeeRepository.save(employee);
    }

//    public List<Employee> getAllEmployees()
//    {
//        List<Employee> all = EmployeeRepository.findAll();
//        return all;
//    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee getEmployeeByUsername(String chatID) {
        return employeeRepository.findByChatID(chatID).orElse(null);
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }


}

package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateEmployeeDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private SendOTP sendOTP;

    public APIResponse<List<EmployeeResponse>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return new APIResponse<>(employees.stream().map(this::toEmployeeResponse).toList(), "Employees retrieved successfully", true);
    }

    public APIResponse<EmployeeResponse> getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Employee not found"));
        return new APIResponse<>(toEmployeeResponse(employee), "Employee retrieved successfully", true);
    }

    public APIResponse<List<EmployeeResponse>> getEmployeeByBranchId(Long branchId) {
        List<Employee> employees = employeeRepository.findByBranch_BranchID(branchId);
        return new APIResponse<>(employees.stream().map(this::toEmployeeResponse).toList(), "Employees retrieved successfully", true);
    }

    public APIResponse<EmployeeResponse> createEmployee(CreateEmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setDob(employeeDTO.getDob());
        employee.setPhone(employeeDTO.getPhone());
        employee.setEmail(employeeDTO.getEmail());
        employee.setAddress(employeeDTO.getAddress());

        Branch branch = branchRepository.findById(employeeDTO.getBranchID()).orElseThrow(() -> new RuntimeException("Branch not found"));
        employee.setBranch(branch);

        Employee newEmployee = employeeRepository.save(employee);

        return new APIResponse<>(toEmployeeResponse(employeeRepository.save(newEmployee)), "Employee created successfully", true);
    }

    public APIResponse<EmployeeResponse> updateEmployee(Long id, CreateEmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employeeDTO.getName() != null) {
            employee.setName(employeeDTO.getName());
        }
        if (employeeDTO.getDob() != null) {
            employee.setDob(employeeDTO.getDob());
        }
        if (employeeDTO.getPhone() != null) {
            employee.setPhone(employeeDTO.getPhone());
        }
        if (employeeDTO.getEmail() != null) {
            employee.setEmail(employeeDTO.getEmail());
        }
        if (employeeDTO.getAddress() != null) {
            employee.setAddress(employeeDTO.getAddress());
        }
        if (employeeDTO.getBranchID() != null) {
            Branch branch = branchRepository.findById(employeeDTO.getBranchID()).orElseThrow(() -> new RuntimeException("Branch not found"));
            employee.setBranch(branch);
        }

        employeeRepository.save(employee);
        return new APIResponse<>(toEmployeeResponse(employee), "Employee updated successfully", true);

    }

    public APIResponse<EmployeeResponse> deleteEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getEmployeeID())) {
            return new APIResponse<>(null, "Employee not found", false);
        }
        employeeRepository.delete(employee);
        return new APIResponse<>(null, "Employee deleted successfully", true);
    }

    public APIResponse<EmployeeResponse> deleteEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            return new APIResponse<>(null, "Employee not found", false);
        }

        employeeRepository.deleteById(id);
        return new APIResponse<>(null, "Employee deleted successfully", true);
    }

    public EmployeeResponse toEmployeeResponse(Employee employee) {
       EmployeeResponse employeeResponse = new EmployeeResponse();
       employeeResponse.setEmployeeID(employee.getEmployeeID());
       employeeResponse.setName(employee.getName());
       employeeResponse.setDob(employee.getDob());
       employeeResponse.setPhone(employee.getPhone());
       employeeResponse.setEmail(employee.getEmail());
       employeeResponse.setAddress(employee.getAddress());
       employeeResponse.setBranchID(employee.getBranch().getBranchID());
       return employeeResponse;
    }
}

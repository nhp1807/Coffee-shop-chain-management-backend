package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateEmployeeDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Employee> employeeExisted = employeeRepository.findById(id);

        if (!employeeExisted.isPresent()) {
            return new APIResponse<>(null, "Employee not found", false);
        }

        return new APIResponse<>(toEmployeeResponse(employeeExisted.get()), "Employee retrieved successfully", true);
    }

    @Transactional
    public APIResponse<EmployeeResponse> updateEmployeeChatId(Long employeeId, String chatId) {
        Optional<Employee> employeeExisted = employeeRepository.findById(employeeId);

        if (!employeeExisted.isPresent()) {
            return new APIResponse<>(null, "Employee not found", false);
        }

        Employee employee = employeeExisted.get();

        employee.setChatID(chatId);

        employee.setChatID(chatId);
        employeeRepository.save(employee);
        return new APIResponse<>(toEmployeeResponse(employee), "Employee chatID updated successfully", true);
    }

    public APIResponse<EmployeeResponse> getEmployeeByEmail(String email) {
        Optional<Employee> employeeExisted = employeeRepository.findByEmail(email);

        if (!employeeExisted.isPresent()) {
            return new APIResponse<>(null, "Employee not found", false);
        }

        return new APIResponse<>(toEmployeeResponse(employeeExisted.get()), "Employee retrieved successfully", true);
    }

    public APIResponse<List<EmployeeResponse>> getEmployeeByBranchId(Long branchId) {
        List<Employee> employees = employeeRepository.findByBranch_BranchID(branchId);

        return new APIResponse<>(employees.stream().map(this::toEmployeeResponse).toList(), "Employees retrieved successfully", true);
    }

    @Transactional
    public APIResponse<EmployeeResponse> createEmployee(CreateEmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setDob(employeeDTO.getDob());
        employee.setPhone(employeeDTO.getPhone());
        employee.setEmail(employeeDTO.getEmail());
        employee.setAddress(employeeDTO.getAddress());
        employee.setShiftSalary(employeeDTO.getShiftSalary());

        Branch branch = branchRepository.findById(employeeDTO.getBranchID()).orElseThrow(() -> new RuntimeException("Branch not found"));
        employee.setBranch(branch);

        Employee newEmployee = employeeRepository.save(employee);

        return new APIResponse<>(toEmployeeResponse(employeeRepository.save(newEmployee)), "Employee created successfully", true);
    }

    @Transactional
    public APIResponse<EmployeeResponse> updateEmployee(Long id, CreateEmployeeDTO employeeDTO) {
        Optional<Employee> employeeExited = employeeRepository.findById(id);
        if (!employeeExited.isPresent()) {
            return new APIResponse<>(null, "Employee not found", false);
        }
        Employee employee = employeeExited.get();

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
        if (employeeDTO.getShiftSalary() != null) {
            employee.setShiftSalary(employeeDTO.getShiftSalary());
        }

        employeeRepository.save(employee);
        return new APIResponse<>(toEmployeeResponse(employee), "Employee updated successfully", true);

    }

    @Transactional
    public APIResponse<EmployeeResponse> deleteEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getEmployeeID())) {
            return new APIResponse<>(null, "Employee not found", false);
        }
        employeeRepository.delete(employee);
        return new APIResponse<>(null, "Employee deleted successfully", true);
    }

    @Transactional
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
        employeeResponse.setShiftSalary(employee.getShiftSalary());
        return employeeResponse;
    }
}

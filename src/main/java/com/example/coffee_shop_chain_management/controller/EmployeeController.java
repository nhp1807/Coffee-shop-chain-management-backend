package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateEmployeeDTO;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.EmployeeResponse;
import com.example.coffee_shop_chain_management.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/get/all")
    public APIResponse<List<EmployeeResponse>> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/get/{id}")
    public APIResponse<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/get/chatID/{chatID}")
    public APIResponse<EmployeeResponse> getEmployeeByChatID(@PathVariable String chatID) {
        return employeeService.getEmployeeByChatID(chatID);
    }

    @PostMapping("/create")
    public APIResponse<EmployeeResponse> createEmployee(@RequestBody CreateEmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);
    }

    @PutMapping("/update/{id}")
    public APIResponse<EmployeeResponse> updateEmployee(@RequestBody CreateEmployeeDTO employeeDTO, @PathVariable Long id) {
        return employeeService.updateEmployee(id, employeeDTO);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<EmployeeResponse> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployeeById(id);
    }


}

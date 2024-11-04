package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateEmployeeDTO;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public Employee createEmployee(@RequestBody CreateEmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);

    }

    @GetMapping("/get/all")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}

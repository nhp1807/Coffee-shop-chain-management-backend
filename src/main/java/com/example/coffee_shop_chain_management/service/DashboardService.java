package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.*;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ExportOrderRepository exportOrderRepository;

    @Autowired
    private ImportOrderRepository importOrderRepository;

    public APIResponse<DashboardResponse> getDashboard() {
        DashboardResponse dashboardResponse = new DashboardResponse();
        List<Branch> branches = branchRepository.findAll();
        List<Employee> employees = employeeRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<ExportOrder> exportOrders = exportOrderRepository.findAll();
        List<ImportOrder> importOrders = importOrderRepository.findAll();

        dashboardResponse.setTotalBranches(branches.size());
        dashboardResponse.setTotalEmployees(employees.size());
        dashboardResponse.setTotalProducts(products.size());
        dashboardResponse.setTotalExportOrders(exportOrders.size());
        dashboardResponse.setTotalImportOrders(importOrders.size());

        double totalRevenue = 0;
        for (ExportOrder exportOrder : exportOrders) {
            totalRevenue += exportOrder.getTotal();
        }

        dashboardResponse.setTotalRevenue(totalRevenue);

        return new APIResponse<>(dashboardResponse, "Dashboard data retrieved successfully", true);
    }
}

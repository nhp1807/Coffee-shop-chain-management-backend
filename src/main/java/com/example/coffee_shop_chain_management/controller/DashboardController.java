package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.AdminDashboardResponse;
import com.example.coffee_shop_chain_management.response.ManagerDashboardResponse;
import com.example.coffee_shop_chain_management.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin/get")
    public APIResponse<AdminDashboardResponse> getAdminDashboard() {
        return dashboardService.getAdminDashboard();
    }

    @GetMapping("/manager/get/{branchID}")
    public APIResponse<ManagerDashboardResponse> getManagerDashboard(@PathVariable Long branchID) {
        return dashboardService.getManagerDashboard(branchID);
    }
}

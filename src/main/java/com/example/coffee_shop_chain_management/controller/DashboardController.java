package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DashboardResponse;
import com.example.coffee_shop_chain_management.service.BranchService;
import com.example.coffee_shop_chain_management.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/get")
    public APIResponse<DashboardResponse> getDashboard() {
        return dashboardService.getDashboard();
    }
}

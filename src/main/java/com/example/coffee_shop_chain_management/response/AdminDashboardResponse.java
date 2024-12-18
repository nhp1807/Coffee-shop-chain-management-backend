package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminDashboardResponse {
    int totalBranches;
    int totalEmployees;
    int totalProducts;
    int totalExportOrders;
    int totalImportOrders;
    double totalRevenue;
}

package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchStatResponse {
    long branchID;
    int totalEmployees;
    int totalExportedOrders;
    long totalExportedOrdersMoney;
    int totalImportedOrders;
    long totalImportedOrdersMoney;
}

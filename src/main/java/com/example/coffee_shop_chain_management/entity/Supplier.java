package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierID;

    private String name;
    private String phone;
    private String address;

    @OneToMany(mappedBy = "supplier")
    private List<ImportOrder> importOrders;

    // Getters and setters
}


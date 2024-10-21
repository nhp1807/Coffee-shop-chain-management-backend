package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchID;

    private String address;
    private String phone;
    private String fax;

    @OneToMany(mappedBy = "branch")
    private List<Employee> employees;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Getters and setters
}


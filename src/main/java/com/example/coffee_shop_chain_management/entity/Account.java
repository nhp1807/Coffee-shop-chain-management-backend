package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    private String username;
    private String password;
    private String role;

    @OneToOne(mappedBy = "account")
    private Branch branch;

    // Getters and setters
}


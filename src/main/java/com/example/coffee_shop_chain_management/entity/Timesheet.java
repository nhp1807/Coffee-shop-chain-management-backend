package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timesheetID;

    private Date date;
    private String shift;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Getters and setters
}


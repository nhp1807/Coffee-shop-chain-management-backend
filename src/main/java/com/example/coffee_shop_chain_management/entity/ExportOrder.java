package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class ExportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exportID;

    private Double total;
    private String paymentMethod;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "exportOrder")
    private List<DetailExportOrder> detailExportOrders;

    // Getters and setters
}


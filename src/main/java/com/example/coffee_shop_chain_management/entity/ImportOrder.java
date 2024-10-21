package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class ImportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long importID;

    private Double total;
    private String paymentMethod;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @OneToMany(mappedBy = "importOrder")
    private List<DetailImportOrder> detailImportOrders;

    // Getters and setters
}


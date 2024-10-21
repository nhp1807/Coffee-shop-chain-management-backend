package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    private String name;
    private String description;
    private Double price;

    @OneToMany(mappedBy = "product")
    private List<DetailExportOrder> detailExportOrders;

    @OneToMany(mappedBy = "product")
    private List<ProductMaterial> productMaterials;

    // Getters and setters
}


package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialID;

    private String name;
    private String description;

    @OneToMany(mappedBy = "material")
    private List<DetailImportOrder> detailImportOrders;

    @OneToMany(mappedBy = "material")
    private List<ProductMaterial> productMaterials;

    @OneToMany(mappedBy = "material")
    private List<Storage> storages;

    // Getters and setters
}


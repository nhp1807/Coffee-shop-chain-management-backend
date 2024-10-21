package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

@Entity
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storageID;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    // Getters and setters
}

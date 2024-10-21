package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ProductMaterialId implements Serializable {

    private Long productId;
    private Long materialId;

    // Constructors, Getters and setters, equals() and hashCode()
}


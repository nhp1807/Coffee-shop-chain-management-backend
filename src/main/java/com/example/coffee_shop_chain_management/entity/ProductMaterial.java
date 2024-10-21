package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

@Entity
public class ProductMaterial {

    @EmbeddedId
    private ProductMaterialId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id")
    private Material material;

    private Integer quantity;

    // Getters and setters
}


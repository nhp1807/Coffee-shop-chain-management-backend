package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_material")
public class ProductMaterial {

    @EmbeddedId
    ProductMaterialId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id")
    Material material;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    // Getters and setters
}


package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productID;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "price", nullable = false)
    Double price;

    @OneToMany(mappedBy = "product")
    List<DetailExportOrder> detailExportOrders;

    @OneToMany(mappedBy = "product")
    List<ProductMaterial> productMaterials;

    // Getters and setters
}


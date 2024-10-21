package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

@Entity
public class DetailImportOrder {

    @EmbeddedId
    private DetailImportOrderId id;

    @ManyToOne
    @MapsId("importOrderId")
    @JoinColumn(name = "import_order_id")
    private ImportOrder importOrder;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id")
    private Material material;

    private Integer quantity;
    private Double price;

    // Getters and setters
}


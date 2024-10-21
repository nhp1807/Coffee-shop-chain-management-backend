package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;

@Entity
public class DetailExportOrder {

    @EmbeddedId
    private DetailExportOrderId id;

    @ManyToOne
    @MapsId("exportOrderId")
    @JoinColumn(name = "export_order_id")
    private ExportOrder exportOrder;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private String description;
    private Double price;

    // Getters and setters
}


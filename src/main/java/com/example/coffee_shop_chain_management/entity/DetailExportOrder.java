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
@Table(name = "detail_export_order")
public class DetailExportOrder {

    @EmbeddedId
    DetailExportOrderId id;

    @ManyToOne
    @MapsId("exportOrderId")
    @JoinColumn(name = "export_order_id")
    ExportOrder exportOrder;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "description")
    String description;

    @Column(name = "price", nullable = false)
    Double price;

    // Getters and setters
}


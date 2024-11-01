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
@Table(name = "detail_import_order")
public class DetailImportOrder {

    @EmbeddedId
    DetailImportOrderId id;

    @ManyToOne
    @MapsId("importOrderId")
    @JoinColumn(name = "import_order_id")
    ImportOrder importOrder;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id")
    Material material;

    @Column(name = "quantity", nullable = false)
    Double quantity;

    @Column(name = "description", nullable = false)
    Double price;

    // Getters and setters
}


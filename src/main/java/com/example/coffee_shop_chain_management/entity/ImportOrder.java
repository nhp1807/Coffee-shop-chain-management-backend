package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "import_order")
public class ImportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long importID;

    @Column(name = "total", nullable = false)
    Double total;

    @Column(name = "payment_method", nullable = false)
    String paymentMethod;

    @Column(name = "date", nullable = false)
    Date date;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    Supplier supplier;

    @OneToMany(mappedBy = "importOrder")
    List<DetailImportOrder> detailImportOrders;

    // Getters and setters
}


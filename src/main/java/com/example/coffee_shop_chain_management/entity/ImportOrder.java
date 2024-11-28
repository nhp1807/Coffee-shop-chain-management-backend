package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
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
    LocalDateTime date;

    @Column(name = "status", nullable = false)
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;

    @OneToMany(mappedBy = "importOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DetailImportOrder> detailImportOrders;

    // Getters and setters

    public ImportOrder(){
        this.status = false;
    }
}


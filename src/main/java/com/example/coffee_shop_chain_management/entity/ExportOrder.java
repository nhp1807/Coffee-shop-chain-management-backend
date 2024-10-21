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
@Table(name = "export_order")
public class ExportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long exportID;

    @Column(name = "total", nullable = false)
    Double total;

    @Column(name = "payment_method", nullable = false)
    String paymentMethod;

    @Column(name = "date", nullable = false)
    Date date;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @OneToMany(mappedBy = "exportOrder")
    List<DetailExportOrder> detailExportOrders;

    // Getters and setters
}


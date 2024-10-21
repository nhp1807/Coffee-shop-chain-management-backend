package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class DetailExportOrderId implements Serializable {

    private Long exportOrderId;
    private Long productId;

    // Constructors, Getters and setters, equals() and hashCode()
}


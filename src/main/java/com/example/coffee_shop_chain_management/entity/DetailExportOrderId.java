package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class DetailExportOrderId implements Serializable {

    Long exportOrderId;
    Long productId;

    // Constructors, Getters and setters, equals() and hashCode()
}


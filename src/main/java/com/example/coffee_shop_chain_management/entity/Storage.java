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
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long storageID;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    Material material;

    // Getters and setters
}

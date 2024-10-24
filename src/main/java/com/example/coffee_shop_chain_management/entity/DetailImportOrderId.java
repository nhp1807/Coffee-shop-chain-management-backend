package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class DetailImportOrderId implements Serializable {

    Long importOrderId;
    Long materialId;

    // Constructors, Getters and setters, equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailImportOrderId that = (DetailImportOrderId) o;
        return Objects.equals(importOrderId, that.importOrderId) &&
                Objects.equals(materialId, that.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(importOrderId, materialId);
    }
}


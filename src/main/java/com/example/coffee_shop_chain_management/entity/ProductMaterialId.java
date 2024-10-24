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
public class ProductMaterialId implements Serializable {

    Long productId;
    Long materialId;

    // Constructors, Getters and setters, equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMaterialId that = (ProductMaterialId) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(materialId, that.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, materialId);
    }
}


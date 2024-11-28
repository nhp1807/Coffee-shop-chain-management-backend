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
public class DetailExportOrderId implements Serializable {

    Long exportOrderId;
    Long productId;

    // Constructors, Getters and setters, equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailExportOrderId that = (DetailExportOrderId) o;
        return Objects.equals(exportOrderId, that.exportOrderId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exportOrderId, productId);
    }
}


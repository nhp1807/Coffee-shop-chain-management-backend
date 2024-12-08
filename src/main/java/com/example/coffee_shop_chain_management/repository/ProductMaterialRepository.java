package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.ProductMaterial;
import com.example.coffee_shop_chain_management.entity.ProductMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, ProductMaterialId> {
    // Truy vấn nếu cần
//    List<ProductMaterial> findByProductID(Long productId);

    List<ProductMaterial> findByProduct_ProductID(Long productId);
}


package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Truy vấn sản phẩm theo tên
    List<Product> findByNameContaining(String name);

    Product findByName(String name);

    boolean existsByName(String name);
}


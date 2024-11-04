package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    // Truy vấn nhà cung cấp theo tên
    List<Supplier> findByNameContaining(String name);

    boolean existsByName(String name);
}


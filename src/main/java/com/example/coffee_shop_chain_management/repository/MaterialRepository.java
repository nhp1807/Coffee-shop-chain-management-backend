package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    // Truy vấn nguyên liệu theo tên
    List<Material> findByNameContaining(String name);
}


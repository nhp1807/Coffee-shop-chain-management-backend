package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}

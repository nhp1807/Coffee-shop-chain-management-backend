package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.ImportOrder;
import com.example.coffee_shop_chain_management.entity.Supplier;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportOrderRepository extends JpaRepository<ImportOrder, Long> {
    // Truy vấn đơn hàng nhập theo nhà cung cấp
    List<ImportOrder> findBySupplier(Supplier supplier);
    List<ImportOrder> findByStatus(Boolean status);
    List<ImportOrder> findByBranch_BranchID(Long branchID);
}


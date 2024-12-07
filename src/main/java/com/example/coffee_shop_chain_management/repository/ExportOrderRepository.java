package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.ExportOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportOrderRepository extends JpaRepository<ExportOrder, Long> {
    // Truy vấn đơn hàng xuất theo nhân viên
    List<ExportOrder> findByEmployee(Employee employee);

    List<ExportOrder> findByBranch_BranchID(Long branchID);
}


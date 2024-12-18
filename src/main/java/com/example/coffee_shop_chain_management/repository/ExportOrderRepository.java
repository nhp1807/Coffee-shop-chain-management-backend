package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.DetailExportOrder;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.ExportOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportOrderRepository extends JpaRepository<ExportOrder, Long> {
    // Truy vấn đơn hàng xuất theo nhân viên
    List<ExportOrder> findByEmployee(Employee employee);

    List<ExportOrder> findByBranch_BranchID(Long branchID);

    @Query(value = "SELECT * FROM export_order e WHERE MONTH(e.date) = ?1 AND YEAR(e.date) = ?2", nativeQuery = true)
    List<ExportOrder> findExportOrderByMonthAndYear(int month, int year);

    @Query(value = "SELECT * FROM export_order e WHERE MONTH(e.date) = ?1 AND YEAR(e.date) = ?2 AND e.branch_id = ?3", nativeQuery = true)
    List<ExportOrder> findExportOrderByMonthAndYearAndBranchID(int month, int year, long branchID);
}


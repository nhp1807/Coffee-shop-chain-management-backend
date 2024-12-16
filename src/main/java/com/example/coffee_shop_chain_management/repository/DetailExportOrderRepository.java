package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.DetailExportOrder;
import com.example.coffee_shop_chain_management.entity.DetailExportOrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetailExportOrderRepository extends JpaRepository<DetailExportOrder, DetailExportOrderId> {
    // Truy vấn chi tiết đơn hàng xuất
    List<DetailExportOrder> findByProduct_ProductID(Long productID);
    DetailExportOrder findDetailExportOrderByExportOrder_ExportIDAndProduct_ProductID(Long exportOrderId, Long productID);
    List<DetailExportOrder> findByProduct_ProductIDAndExportOrder_DateBetween(
            Long productID, LocalDateTime startDateTime, LocalDateTime endDateTime
    );
}


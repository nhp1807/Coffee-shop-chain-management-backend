package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.DetailImportOrder;
import com.example.coffee_shop_chain_management.entity.DetailImportOrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailImportOrderRepository extends JpaRepository<DetailImportOrder, DetailImportOrderId> {
    DetailImportOrder findDetailImportOrderByImportOrder_ImportIDAndMaterial_MaterialID(Long importOrderId, Long materialId);
}

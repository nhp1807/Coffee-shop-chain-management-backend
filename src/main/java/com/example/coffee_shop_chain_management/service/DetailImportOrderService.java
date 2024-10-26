package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.DetailImportOrder;
import com.example.coffee_shop_chain_management.entity.DetailImportOrderId;
import com.example.coffee_shop_chain_management.repository.DetailImportOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailImportOrderService {
    @Autowired
    private DetailImportOrderRepository detailImportOrderRepository;

    public DetailImportOrder createDetailImportOrder(DetailImportOrder detailImportOrder){
        return detailImportOrderRepository.save(detailImportOrder);
    }

    public DetailImportOrder getDetailImportOrderById(DetailImportOrderId id){
        return detailImportOrderRepository.findById(id).orElse(null);
    }

    public DetailImportOrder updateDetailImportOrder(DetailImportOrder detailImportOrder){
        return detailImportOrderRepository.save(detailImportOrder);
    }

    public void deleteDetailImportOrder(DetailImportOrder detailImportOrder){
        detailImportOrderRepository.delete(detailImportOrder);
    }

    public void deleteDetailImportOrderById(DetailImportOrderId id){
        detailImportOrderRepository.deleteById(id);
    }
}

package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.DetailExportOrder;
import com.example.coffee_shop_chain_management.entity.DetailExportOrderId;
import com.example.coffee_shop_chain_management.repository.DetailExportOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailExportOrderService {
    @Autowired
    private DetailExportOrderRepository detailExportOrderRepository;

    public List<DetailExportOrder> getAllDetailExportOrders(){
        return detailExportOrderRepository.findAll();
    }

    public DetailExportOrder createDetailExportOrder(DetailExportOrder detailExportOrder){
        return detailExportOrderRepository.save(detailExportOrder);
    }

    public DetailExportOrder getDetailExportOrderById(DetailExportOrderId id){
        return detailExportOrderRepository.findById(id).orElse(null);
    }

    public DetailExportOrder updateDetailExportOrder(DetailExportOrder detailExportOrder){
        return detailExportOrderRepository.save(detailExportOrder);
    }

    public void deleteDetailExportOrder(DetailExportOrder detailExportOrder){
        detailExportOrderRepository.delete(detailExportOrder);
    }

    public void deleteDetailExportOrderById(DetailExportOrderId id){
        detailExportOrderRepository.deleteById(id);
    }
}

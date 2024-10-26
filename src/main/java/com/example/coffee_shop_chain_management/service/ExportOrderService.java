package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.ExportOrder;
import com.example.coffee_shop_chain_management.repository.ExportOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportOrderService {
    @Autowired
    private ExportOrderRepository exportOrderRepository;

    public ExportOrder createExportOrder(ExportOrder exportOrder) {
        return exportOrderRepository.save(exportOrder);
    }

    public ExportOrder getExportOrderById(Long id) {
        return exportOrderRepository.findById(id).orElse(null);
    }

    public ExportOrder updateExportOrder(ExportOrder exportOrder) {
        return exportOrderRepository.save(exportOrder);
    }

    public void deleteExportOrder(ExportOrder exportOrder) {
        exportOrderRepository.delete(exportOrder);
    }

    public void deleteExportOrderByID(Long id) {
        exportOrderRepository.deleteById(id);
    }
}

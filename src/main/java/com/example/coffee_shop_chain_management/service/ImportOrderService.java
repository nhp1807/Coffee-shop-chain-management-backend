package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.ImportOrder;
import com.example.coffee_shop_chain_management.repository.ImportOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportOrderService {
    @Autowired
    private ImportOrderRepository importOrderRepository;

    public ImportOrder createImportOrder(ImportOrder importOrder) {
        return importOrderRepository.save(importOrder);
    }

    public ImportOrder getImportOrderById(Long id) {
        return importOrderRepository.findById(id).orElse(null);
    }

    public ImportOrder updateImportOrder(ImportOrder importOrder) {
        return importOrderRepository.save(importOrder);
    }

    public void deleteImportOrder(ImportOrder importOrder) {
        importOrderRepository.delete(importOrder);
    }

    public void deleteImportOrderByID(Long id) {
        importOrderRepository.deleteById(id);
    }
}

package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateImportOrderDTO;
import com.example.coffee_shop_chain_management.dto.DetailImportOrderDTO;
import com.example.coffee_shop_chain_management.entity.ImportOrder;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.ImportOrderResponse;
import com.example.coffee_shop_chain_management.service.ImportOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/import-order")
public class ImportOrderController {
    private final ImportOrderService importOrderService;

    @Autowired
    public ImportOrderController(ImportOrderService importOrderService) {
        this.importOrderService = importOrderService;
    }

    @GetMapping("/get/all")
    public List<ImportOrder> getAllImportOrders() {
        return importOrderService.getAllImportOrders();
    }

    @GetMapping("/get/{id}")
    public ImportOrder getImportOrderById(@PathVariable Long id) {
        return importOrderService.getImportOrderById(id);
    }

    @PostMapping("/create")
    public APIResponse<ImportOrderResponse> createImportOrder(@RequestBody CreateImportOrderDTO importOrderDTO) {
        return importOrderService.createImportOrder(importOrderDTO);
    }

//    @PutMapping("/update/{id}")
//    public APIResponse<ImportOrderResponse> updateImportOrder(@RequestBody CreateImportOrderDTO importOrderDTO, @PathVariable Long id) {
//        return importOrderService.updateImportOrder(id, importOrderDTO);
//    }

    @PutMapping("/add/{id}")
    public APIResponse<ImportOrderResponse> addDetailImportOrder(@RequestBody DetailImportOrderDTO importOrderDTO, @PathVariable Long id) {
        return importOrderService.addDetailImportOrder(id, importOrderDTO);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<ImportOrderResponse> deleteImportOrder(@PathVariable Long id) {
        return importOrderService.deleteImportOrderById(id);
    }
}

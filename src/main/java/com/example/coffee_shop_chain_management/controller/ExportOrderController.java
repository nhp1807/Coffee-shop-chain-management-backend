package com.example.coffee_shop_chain_management.controller;


import com.example.coffee_shop_chain_management.dto.CreateExportOrderDTO;
import com.example.coffee_shop_chain_management.entity.ExportOrder;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.ExportOrderResponse;
import com.example.coffee_shop_chain_management.service.ExportOrderService;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/export-order")

public class ExportOrderController {
    private final ExportOrderService exportOrderService;

    @Autowired
    public ExportOrderController(ExportOrderService exportOrderService) {
        this.exportOrderService = exportOrderService;
    }

    @GetMapping("/get/all")
    public APIResponse<List<ExportOrderResponse>> getAllExportOrders() {
        return exportOrderService.getAllExportOrders();
    }

    @GetMapping("/get/{id}")
    public APIResponse<ExportOrderResponse> getExportOrderById(@PathVariable Long id) {
        return exportOrderService.getExportOrderById(id);
    }

    @PostMapping("/create")
    public APIResponse<ExportOrderResponse> createExportOrder(@RequestBody CreateExportOrderDTO exportOrderDTO) {
        return exportOrderService.createExportOrder(exportOrderDTO);
    }

    @PutMapping("/update/{id}")
    public APIResponse<ExportOrderResponse> updateExportOrder(@RequestBody CreateExportOrderDTO exportOrderDTO, @PathVariable Long id) {
        return exportOrderService.updateExportOrder(id, exportOrderDTO);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<ExportOrderResponse> deleteExportOrder(@PathVariable Long id) {
        return exportOrderService.deleteExportOrderById(id);
    }
}

package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.entity.DetailImportOrder;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DetailImportOrderResponse;
import com.example.coffee_shop_chain_management.service.DetailImportOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/import-order-detail")
public class DetailImportOrderController {
    @Autowired
    private DetailImportOrderService detailImportOrderService;

    @GetMapping("/get/{importOrderId}/{materialId}")
    public APIResponse<DetailImportOrderResponse> getDetailImportOrderById(@PathVariable Long importOrderId, @PathVariable Long materialId){
        return detailImportOrderService.getDetailImportOrderById(importOrderId, materialId);
    }

    @PutMapping("/update/{importOrderId}/{materialId}")
    public APIResponse<DetailImportOrderResponse> updateDetailImportOrder(@PathVariable Long importOrderId, @PathVariable Long materialId, @RequestBody DetailImportOrder detailImportOrder){
        return detailImportOrderService.updateDetailImportOrder(importOrderId, materialId, detailImportOrder);
    }
}

package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.DetailImportOrderDTO;
import com.example.coffee_shop_chain_management.entity.DetailImportOrder;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DetailImportOrderResponse;
import com.example.coffee_shop_chain_management.response.ImportOrderResponse;
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

    @PutMapping("/add/{id}")
    public APIResponse<ImportOrderResponse> addDetailImportOrder(@RequestBody DetailImportOrderDTO importOrderDTO, @PathVariable Long id) {
        return detailImportOrderService.addDetailImportOrder(id, importOrderDTO);
    }

    @DeleteMapping("/delete/{orderID}/{materialID}")
    public APIResponse<ImportOrderResponse> deleteDetailImportOrder(@PathVariable Long orderID, @PathVariable Long materialID) {
        return detailImportOrderService.deleteDetailImportOrder(orderID, materialID);
    }
}

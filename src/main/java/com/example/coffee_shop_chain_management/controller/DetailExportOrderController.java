package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.DetailExportOrderDTO;
import com.example.coffee_shop_chain_management.entity.DetailExportOrder;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DetailExportOrderResponse;
import com.example.coffee_shop_chain_management.service.DetailExportOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/export-order-detail")
public class DetailExportOrderController {
    @Autowired
    private DetailExportOrderService detailExportOrderService;

    @GetMapping("/get/{exportOrderId}/{productId}")
    public APIResponse<DetailExportOrderResponse> getDetailExportOrderById(@PathVariable Long exportOrderId, @PathVariable Long productId){
        return detailExportOrderService.getDetailExportOrderById(exportOrderId, productId);
    }

    @PutMapping("/update/{exportOrderId}/{productId}")
    public APIResponse<DetailExportOrderResponse> updateDetailExportOrder(@PathVariable Long exportOrderId, @PathVariable Long productId, @RequestBody DetailExportOrder detailImportOrder){
        return detailExportOrderService.updateDetailExportOrder(exportOrderId, productId, detailImportOrder);
    }

    @PutMapping("/add/{id}")
    public APIResponse<DetailExportOrderResponse> addDetailExportOrder(@RequestBody DetailExportOrderDTO importOrderDTO, @PathVariable Long id) {
        return detailExportOrderService.addDetailExportOrder(id, importOrderDTO);
    }

    @DeleteMapping("/delete/{orderID}/{materialID}")
    public APIResponse<DetailExportOrderResponse> deleteDetailExportOrder(@PathVariable Long orderID, @PathVariable Long materialID) {
        return detailExportOrderService.deleteDetailExportOrder(orderID, materialID);
    }
}

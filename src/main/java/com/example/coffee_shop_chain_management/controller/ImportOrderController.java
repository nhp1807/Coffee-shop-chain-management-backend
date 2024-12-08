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
    public APIResponse<List<ImportOrderResponse>> getAllImportOrders() {
        return importOrderService.getAllImportOrders();
    }

    @GetMapping("/get/{id}")
    public APIResponse<ImportOrderResponse> getImportOrderById(@PathVariable Long id) {
        return importOrderService.getImportOrderById(id);
    }

    @GetMapping("/get/branch/{branchID}")
    public APIResponse<List<ImportOrderResponse>> getImportOrderByBranchId(@PathVariable Long branchID) {
        return importOrderService.getImportOrderByBranchId(branchID);
    }

    @GetMapping("/get/un-confirmed")
    public APIResponse<List<ImportOrderResponse>> getUnconfirmedImportOrders() {
        return importOrderService.getImportOrderByStatus(false);
    }

    @PostMapping("/create")
    public APIResponse<ImportOrderResponse> createImportOrder(@RequestBody CreateImportOrderDTO importOrderDTO) {
        return importOrderService.createImportOrder(importOrderDTO);
    }

    @PutMapping("/add/{id}")
    public APIResponse<ImportOrderResponse> addDetailImportOrder(@RequestBody DetailImportOrderDTO importOrderDTO, @PathVariable Long id) {
        return importOrderService.addDetailImportOrder(id, importOrderDTO);
    }

//    @PutMapping("/update/{orderID}/{materialID}")
//    public APIResponse<ImportOrderResponse> updateDetailImportOrder(@PathVariable Long orderID, @PathVariable Long materialID, @RequestBody DetailImportOrderDTO importOrderDTO) {
//        return importOrderService.updateDetailImportOrder(orderID, materialID, importOrderDTO);
//    }

    @DeleteMapping("/delete/{orderID}/{materialID}")
    public APIResponse<ImportOrderResponse> deleteDetailImportOrder(@PathVariable Long orderID, @PathVariable Long materialID) {
        return importOrderService.deleteDetailImportOrder(orderID, materialID);
    }

    @PostMapping("/confirm/{id}")
    public APIResponse<ImportOrderResponse> confirmImportOrder(@PathVariable Long id) {
        return importOrderService.confirmImportOrder(id);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<ImportOrderResponse> deleteImportOrder(@PathVariable Long id) {
        return importOrderService.deleteImportOrderById(id);
    }
}

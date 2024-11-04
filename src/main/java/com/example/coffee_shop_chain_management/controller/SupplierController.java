package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateSupplierDTO;
import com.example.coffee_shop_chain_management.dto.UpdateSupplierDTO;
import com.example.coffee_shop_chain_management.entity.Supplier;
import com.example.coffee_shop_chain_management.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/get/all")
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/get/{id}")
    public Supplier getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id);
    }

    @PostMapping("/create")
    public Supplier createSupplier(@RequestBody CreateSupplierDTO supplier) {
        return supplierService.createSupplier(supplier);
    }

    @PutMapping("/update/{id}")
    public Supplier updateSupplier(@RequestBody UpdateSupplierDTO supplierDTO, @PathVariable Long id) {
        return supplierService.updateSupplier(id, supplierDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplierById(id);
    }
}

package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.StorageResponse;
import com.example.coffee_shop_chain_management.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @GetMapping("/get/all")
    public APIResponse<List<StorageResponse>> getAllStorages() {
        return storageService.getAllStorages();
    }

    @GetMapping("/get/{id}")
    public APIResponse<StorageResponse> getStorageById(@PathVariable Long id) {
        return storageService.getStorageById(id);
    }

    @GetMapping("/get/branch/{branchID}")
    public APIResponse<List<StorageResponse>> getStorageByBranchId(@PathVariable Long branchID) {
        return storageService.getStorageByBranchId(branchID);
    }
}

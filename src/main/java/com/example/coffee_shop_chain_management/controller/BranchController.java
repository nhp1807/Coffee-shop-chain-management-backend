package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateBranchDTO;
import com.example.coffee_shop_chain_management.dto.UpdateBranchDTO;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.BranchResponse;
import com.example.coffee_shop_chain_management.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/branch")
public class BranchController {
    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/get/all")
    public APIResponse<List<BranchResponse>> getAllBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/get/{id}")
    public APIResponse<BranchResponse> getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id);
    }

    @PostMapping("/create")
    public APIResponse<BranchResponse> createBranch(@RequestBody CreateBranchDTO branchDTO) {
        return branchService.createBranch(branchDTO);
    }

    @PutMapping("/update/{id}")
    public APIResponse<BranchResponse> updateBranch(@RequestBody UpdateBranchDTO branchDTO, @PathVariable Long id) {
        return branchService.updateBranch(id, branchDTO);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<BranchResponse> deleteBranch(@PathVariable Long id) {
        return branchService.deleteBranchById(id);
    }
}

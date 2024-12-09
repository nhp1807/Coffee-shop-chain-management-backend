package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateBranchDTO;
import com.example.coffee_shop_chain_management.dto.DateRangeDTO;
import com.example.coffee_shop_chain_management.dto.UpdateBranchDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.BranchRepository;

import com.example.coffee_shop_chain_management.repository.ExportOrderRepository;
import com.example.coffee_shop_chain_management.repository.ImportOrderRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.BranchResponse;
import com.example.coffee_shop_chain_management.response.BranchStatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ExportOrderRepository exportOrderRepository;

    @Autowired
    private ImportOrderRepository importOrderRepository;

    public APIResponse<List<BranchResponse>> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();

        return new APIResponse<>(branches.stream().map(this::toBranchResponse).toList(), "Branches retrieved successfully", true);
    }

    @Transactional
    public APIResponse<BranchResponse> createBranch(CreateBranchDTO branchDTO) {
        if (branchRepository.existsByAddress(branchDTO.getAddress())) {
            return new APIResponse<>(null, "Branch already exists", false);
        }

        Branch branch = new Branch();
        branch.setAddress(branchDTO.getAddress());
        branch.setPhone(branchDTO.getPhone());
        branch.setFax(branchDTO.getFax());

        Branch newBranch = branchRepository.save(branch);

        List<Material> materials = new ArrayList<>();
        for (Material material : materials) {
            Storage storage = new Storage();
            storage.setBranch(newBranch);
            storage.setMaterial(material);
            storage.setQuantity(0D);
        }

        return new APIResponse<>(toBranchResponse(newBranch), "Branch created successfully", true);
    }

    public APIResponse<BranchResponse> getBranchById(Long id) {
        Optional<Branch> branchExisted = branchRepository.findById(id);

        if (!branchExisted.isPresent()) {
            return new APIResponse<>(null, "Branch not found", false);
        }

        return new APIResponse<>(toBranchResponse(branchExisted.get()), "Branch retrieved successfully", true);
    }

    @Transactional
    public APIResponse<BranchResponse> updateBranch(Long id, UpdateBranchDTO branchDTO) {
        Optional<Branch> branchExisted = branchRepository.findById(id);

        if (!branchExisted.isPresent()) {
            return new APIResponse<>(null, "Branch not found", false);
        }

        Branch branch = branchExisted.get();

        if (branchDTO.getAddress() != null) {
            branch.setAddress(branchDTO.getAddress());
        }

        if (branchDTO.getPhone() != null) {
            branch.setPhone(branchDTO.getPhone());
        }

        if (branchDTO.getFax() != null) {
            branch.setFax(branchDTO.getFax());
        }

        branchRepository.save(branch);

        return new APIResponse<>(toBranchResponse(branch), "Branch updated successfully", true);
    }
  
    @Transactional
    public APIResponse<BranchResponse> deleteBranch(Branch branch) {
        if (!branchRepository.existsById(branch.getBranchID())) {
            return new APIResponse<>(null, "Branch not found", false);
        }

        branchRepository.delete(branch);
        return new APIResponse<>(null, "Branch deleted successfully", true);
    }

    @Transactional
    public APIResponse<BranchResponse> deleteBranchById(Long id) {
        if (!branchRepository.existsById(id)) {
            return new APIResponse<>(null, "Branch not found", false);
        }

        branchRepository.deleteById(id);
        return new APIResponse<>(null, "Branch deleted successfully", true);
    }

    public APIResponse<List<BranchStatResponse>> getAllBranchStats(DateRangeDTO dateRangeDTO) {
        List<Branch> branches = branchRepository.findAll();
        List<BranchStatResponse> branchStatResponses = new ArrayList<>();

        for (Branch branch : branches) {
            BranchStatResponse branchStatResponse = toBranchStatResponse(branch, dateRangeDTO);

            branchStatResponses.add(branchStatResponse);
        }

        return new APIResponse<>(branchStatResponses, "Branch stats retrieved successfully", true);
    }

    public APIResponse<BranchStatResponse> getBranchStat(Long id, DateRangeDTO dateRangeDTO) {
        Optional<Branch> branchExisted = branchRepository.findById(id);

        if (!branchExisted.isPresent()) {
            return new APIResponse<>(null, "Branch not found", false);
        }

        Branch branch = branchExisted.get();

        BranchStatResponse branchStatResponse = toBranchStatResponse(branch, dateRangeDTO);

        return new APIResponse<>(branchStatResponse, "Branch stat retrieved successfully", true);
    }

    public BranchResponse toBranchResponse(Branch branch) {
        BranchResponse branchResponse = new BranchResponse();
        branchResponse.setBranchID(branch.getBranchID());
        branchResponse.setAddress(branch.getAddress());
        branchResponse.setPhone(branch.getPhone());
        branchResponse.setFax(branch.getFax());
        return branchResponse;
    }

    public BranchStatResponse toBranchStatResponse(Branch branch, DateRangeDTO dateRangeDTO) {
        LocalDateTime startDate = (dateRangeDTO.getStartDate() != null
                ? dateRangeDTO.getStartDate().atStartOfDay()
                : LocalDateTime.MIN);
        LocalDateTime endDate = (dateRangeDTO.getEndDate() != null
                ? dateRangeDTO.getEndDate().atTime(LocalTime.MAX)
                : LocalDateTime.MAX);

        BranchStatResponse branchStatResponse = new BranchStatResponse();
        branchStatResponse.setBranchID(branch.getBranchID());
        branchStatResponse.setTotalEmployees(branch.getEmployees().size());

        long totalExportedOrdersMoney = 0;
        long totalImportedOrdersMoney = 0;

        List<ExportOrder> exportOrders = exportOrderRepository.findByBranch_BranchID(branch.getBranchID());
        for (ExportOrder exportOrder : exportOrders) {
            if (exportOrder.getDate().isAfter(startDate) && exportOrder.getDate().isBefore(endDate)) {
                totalExportedOrdersMoney += exportOrder.getTotal();
            }
        }

        List<ImportOrder> importOrders = importOrderRepository.findByBranch_BranchID(branch.getBranchID());
        for (ImportOrder importOrder : importOrders) {
            if (importOrder.getDate().isAfter(startDate) && importOrder.getDate().isBefore(endDate)) {
                totalImportedOrdersMoney += importOrder.getTotal();
            }
        }

        branchStatResponse.setTotalExportedOrders(exportOrders.size());
        branchStatResponse.setTotalExportedOrdersMoney(totalExportedOrdersMoney);
        branchStatResponse.setTotalImportedOrders(importOrders.size());
        branchStatResponse.setTotalImportedOrdersMoney(totalImportedOrdersMoney);

        return branchStatResponse;
    }
}

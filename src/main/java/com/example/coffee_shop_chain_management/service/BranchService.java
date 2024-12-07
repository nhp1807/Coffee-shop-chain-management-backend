package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateBranchDTO;
import com.example.coffee_shop_chain_management.dto.UpdateBranchDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.entity.Material;
import com.example.coffee_shop_chain_management.entity.Storage;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.BranchRepository;

import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.BranchResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public APIResponse<List<BranchResponse>> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();

        return new APIResponse<>(branches.stream().map(this::toBranchResponse).toList(), "Branches retrieved successfully", true);
    }

    public APIResponse<BranchResponse> getBranchById(Long id) {
        Optional<Branch> branch = branchRepository.findById(id);
        if (!branch.isPresent()) {
            return new APIResponse<>(null, "Branch not found", false);
        }
        return new APIResponse<>(toBranchResponse(branch.get()), "Branch retrieved successfully", true);
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


    @Transactional
    public APIResponse<BranchResponse> updateBranch(Long id, UpdateBranchDTO branchDTO) {
        Optional<Branch> branchExited = branchRepository.findById(id);
        if (!branchExited.isPresent()) {
            return new APIResponse<>(null, "Branch not found", false);
        }

        Branch branch = branchExited.get();

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

    public BranchResponse toBranchResponse(Branch branch) {
        BranchResponse branchResponse = new BranchResponse();
        branchResponse.setBranchID(branch.getBranchID());
        branchResponse.setAddress(branch.getAddress());
        branchResponse.setPhone(branch.getPhone());
        branchResponse.setFax(branch.getFax());
        return branchResponse;
    }
}

package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateBranchDTO;
import com.example.coffee_shop_chain_management.dto.UpdateBranchDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.BranchRepository;

import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.BranchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public APIResponse<BranchResponse> createBranch(CreateBranchDTO branchDTO) {
        if (branchRepository.existsByAddress(branchDTO.getAddress())) {
            return new APIResponse<>(null, "Branch already exists", false);
        }

        Branch branch = new Branch();
        branch.setAddress(branchDTO.getAddress());
        branch.setPhone(branchDTO.getPhone());
        branch.setFax(branchDTO.getFax());
        Account account = accountRepository.findById(branchDTO.getAccountId()).orElse(null);
        branch.setAccount(account);

        Branch newBranch = branchRepository.save(branch);

        return new APIResponse<>(toBranchResponse(newBranch), "Branch created successfully", true);
    }

    public APIResponse<BranchResponse> getBranchById(Long id) {
        Branch branch = branchRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Branch not found!"));

        return new APIResponse<>(toBranchResponse(branch), "Branch retrieved successfully", true);
    }

    public APIResponse<BranchResponse> updateBranch(Long id, UpdateBranchDTO branchDTO) {
        Branch branch = branchRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Branch not found!"));

        if (branchDTO.getAddress() != null) {
            branch.setAddress(branchDTO.getAddress());
        }

        if (branchDTO.getPhone() != null) {
            branch.setPhone(branchDTO.getPhone());
        }

        if (branchDTO.getFax() != null) {
            branch.setFax(branchDTO.getFax());
        }

        if (branchDTO.getAccountId() != null) {
            if (!branchDTO.getAccountId().equals(branch.getAccount().getAccountID())) {
                Account account = accountRepository.findById(branchDTO.getAccountId()).orElse(null);
                // Check any branch have this account
                List<Branch> branches = branchRepository.findAll();
                for (Branch b : branches) {
                    if (Objects.equals(b.getAccount().getAccountID(), branchDTO.getAccountId())) {
                        return new APIResponse<>(toBranchResponse(branch), "This account has been used in other branch", false);
                    }
                }
                branch.setAccount(account);
            }
        }

        branchRepository.save(branch);

        return new APIResponse<>(toBranchResponse(branch), "Branch updated successfully", true);
    }

    public APIResponse<BranchResponse> deleteBranch(Branch branch) {
        if (!branchRepository.existsById(branch.getBranchID())) {
            return new APIResponse<>(null, "Branch not found", false);
        }

        branchRepository.delete(branch);
        return new APIResponse<>(null, "Branch deleted successfully", true);
    }

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
        branchResponse.setAccountId(branch.getAccount().getAccountID());
        return branchResponse;
    }
}

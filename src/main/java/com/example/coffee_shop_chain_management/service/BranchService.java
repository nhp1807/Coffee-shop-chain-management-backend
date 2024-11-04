package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateBranchDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.BranchRepository;

import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.BranchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Branch> getAllBranches(){
        return branchRepository.findAll();
    }

    public APIResponse<BranchResponse> createBranch(CreateBranchDTO branchDTO){
        if (branchRepository.existsByAddress(branchDTO.getAddress())){
            return null;
        }

        Branch branch = new Branch();
        branch.setAddress(branchDTO.getAddress());
        branch.setPhone(branchDTO.getPhone());
        branch.setFax(branchDTO.getFax());
        Account account = accountRepository.findById(branchDTO.getAccountId()).orElse(null);
        branch.setAccount(account);

        Branch newBranch =  branchRepository.save(branch);

        BranchResponse branchResponse = new BranchResponse();
        branchResponse.setBranchID(newBranch.getBranchID());
        branchResponse.setAddress(newBranch.getAddress());
        branchResponse.setPhone(newBranch.getPhone());
        branchResponse.setFax(newBranch.getFax());
        branchResponse.setAccountId(newBranch.getAccount().getAccountID());

        return new APIResponse<>(branchResponse, "Branch created successfully", true);
    }

    public Branch getBranchById(Long id){
        return branchRepository.findById(id).orElse(null);
    }

    public Branch updateBranch(Branch branch){
        return branchRepository.save(branch);
    }

    public void deleteBranch(Branch branch){
        branchRepository.delete(branch);
    }

    public void deleteBranchById(Long id){
        branchRepository.deleteById(id);
    }
}

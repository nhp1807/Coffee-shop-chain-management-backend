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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<BranchResponse> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();

        // Chuyển đổi danh sách Branch sang danh sách BranchResponse
        return branches.stream().map(branch -> {
            BranchResponse branchResponse = new BranchResponse();
            branchResponse.setBranchID(branch.getBranchID());
            branchResponse.setAddress(branch.getAddress());
            branchResponse.setPhone(branch.getPhone());
            branchResponse.setFax(branch.getFax());
            branchResponse.setAccountId(branch.getAccount().getAccountID());
            return branchResponse;
        }).collect(Collectors.toList());
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

    public BranchResponse getBranchById(Long id) {
        Branch branch = branchRepository.findById(id).orElse(null);

        // Nếu branch không tồn tại, trả về null
        if (branch == null) {
            return null;
        }

        // Tạo đối tượng BranchResponse và gán giá trị từ branch
        BranchResponse branchResponse = new BranchResponse();
        branchResponse.setBranchID(branch.getBranchID());
        branchResponse.setAddress(branch.getAddress());
        branchResponse.setPhone(branch.getPhone());
        branchResponse.setFax(branch.getFax());
        branchResponse.setAccountId(branch.getAccount().getAccountID());

        return branchResponse;
    }


    public Branch updateBranch(Long branchId, UpdateBranchDTO branchDTO){
        Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found!"));

        if (branchDTO.getAddress() != null) {
            existingBranch.setAddress(branchDTO.getAddress());
        }

        if (branchDTO.getPhone() != null) {
            existingBranch.setPhone(branchDTO.getPhone());
        }

        if (branchDTO.getFax() != null) {
            existingBranch.setFax(branchDTO.getFax());
        }

        branchRepository.save(existingBranch);
        return existingBranch;
    }

    public boolean deleteBranch(Branch branch){
        if (!branchRepository.existsById(branch.getBranchID())) {
            return false;
        }
        branchRepository.delete(branch);
        return true;
    }

    public boolean deleteBranchById(Long id){
        if (!branchRepository.existsById(id)) {
            return false;
        }
        branchRepository.deleteById(id);
        return true;
    }
}

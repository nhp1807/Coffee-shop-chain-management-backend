package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.repository.BranchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    public Branch createBranch(Branch branch){
        return branchRepository.save(branch);
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

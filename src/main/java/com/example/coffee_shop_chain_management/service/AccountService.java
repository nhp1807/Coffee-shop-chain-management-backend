package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.AccountResponse;
import com.example.coffee_shop_chain_management.response.BranchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public APIResponse<List<AccountResponse>> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();

        return new APIResponse<>(accounts.stream().map(this::toAccountResponse).toList(), "Accounts retrieved successfully!", true);
    }

    public APIResponse<AccountResponse> createAccount(CreateAccountDTO accountDTO){
        if(accountRepository.existsByUsername(accountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }

        Account account = new Account();
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setEmail(accountDTO.getEmail());
        account.setRole(accountDTO.getRole());

        Branch branch =  branchRepository.findById(accountDTO.getBranchID()).get();
        account.setBranch(branch);

        Account newAccount = accountRepository.save(account);

        return new APIResponse<>(toAccountResponse(newAccount), "Account created successfully!", true);
    }

    public APIResponse<AccountResponse> getAccountById(Long id){
        Account account = accountRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Account not found!"));

        return new APIResponse<>(toAccountResponse(account), "Account retrieved successfully!", true);
    }

    public APIResponse<AccountResponse> getAccountByUsername(String username){
        Account account = accountRepository.findByUsername(username).
                orElseThrow(() -> new RuntimeException("Account not found!"));

        return new APIResponse<>(toAccountResponse(account), "Account retrieved successfully!", true);
    }

    public APIResponse<AccountResponse> getAccountByEmail(String email){
        Account account = accountRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Account not found!"));

        return new APIResponse<>(toAccountResponse(account), "Account retrieved successfully!", true);
    }

    public APIResponse<AccountResponse> updateAccount(Long accountID, UpdateAccountDTO accountDTO){
        Account account = accountRepository.findById(accountID).
                orElseThrow(() -> new RuntimeException("Account not found!"));

        if (accountDTO.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        }

        if (accountDTO.getEmail() != null) {
            account.setEmail(accountDTO.getEmail());
        }

        if (accountDTO.getChatID() != null) {
            account.setChatID(accountDTO.getChatID());
        }

        if (accountDTO.getBranchID() != null) {
            Branch branch =  branchRepository.findById(accountDTO.getBranchID()).get();
            account.setBranch(branch);
        }

        accountRepository.save(account);

        return new APIResponse<>(toAccountResponse(account), "Account updated successfully!", true);
    }

    public APIResponse<AccountResponse> deleteAccount(Account account){
        if (!accountRepository.existsById(account.getAccountID())) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        accountRepository.delete(account);
        return new APIResponse<>(null, "Account deleted successfully!", true);
    }

    public APIResponse<AccountResponse> deleteAccountById(Long id){
        if (!accountRepository.existsById(id)) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        accountRepository.deleteById(id);
        return new APIResponse<>(null, "Account deleted successfully!", true);
    }

    public AccountResponse toAccountResponse(Account account){
        AccountResponse accountResponse = new AccountResponse();

        accountResponse.setAccountID(account.getAccountID());
        accountResponse.setUsername(account.getUsername());
        accountResponse.setRole(account.getRole());
        accountResponse.setChatID(account.getChatID());
        accountResponse.setEmail(account.getEmail());
        if (account.getBranch() != null){
            accountResponse.setBranchID(account.getBranch().getBranchID());
        }

        return accountResponse;
    }
}

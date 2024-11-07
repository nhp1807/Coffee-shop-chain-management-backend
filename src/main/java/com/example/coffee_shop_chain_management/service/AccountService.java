package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.response.AccountResponse;
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
    private PasswordEncoder passwordEncoder;

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();

        // Chuyển đổi danh sách Account thành danh sách AccountResponse
        return accounts.stream().map(account -> {
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setAccountId(account.getAccountID());
            accountResponse.setUsername(account.getUsername());
            accountResponse.setEmail(account.getEmail());
            accountResponse.setRole(account.getRole());
            accountResponse.setChatID(account.getChatID());

            // Kiểm tra nếu branch không phải null, thì mới lấy branchID
            if (account.getBranch() != null) {
                accountResponse.setBranchID(account.getBranch().getBranchID());
            } else {
                accountResponse.setBranchID(null);
            }

            return accountResponse;
        }).collect(Collectors.toList());
    }


    public Account createAccount(CreateAccountDTO accountDTO){
        if(accountRepository.existsByUsername(accountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }

        Account account = new Account();
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setEmail(accountDTO.getEmail());
        account.setRole(accountDTO.getRole());
        account.setChatID(accountDTO.getChatID());

        return accountRepository.save(account);
    }

    public AccountResponse getAccountById(Long id){
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new RuntimeException("Account not found!");
        }

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(account.getAccountID());
        accountResponse.setUsername(account.getUsername());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setRole(account.getRole());
        accountResponse.setChatID(account.getChatID());
        accountResponse.setBranchID(account.getBranch().getBranchID());

        return accountResponse;

    }

    public Account getAccountByUsername(String username){
        return accountRepository.findByUsername(username).orElse(null);
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findByEmail(email).orElse(null);
    }

    public Account updateAccount(Long accountID, UpdateAccountDTO accountDTO){
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

        accountRepository.save(account);

        return account;
    }

    public boolean deleteAccount(Account account){
        if (!accountRepository.existsById(account.getAccountID())) {
            return false;
        }

        accountRepository.delete(account);
        return true;
    }

    public boolean deleteAccountById(Long id){
        if (!accountRepository.existsById(id)) {
            return false;
        }

        accountRepository.deleteById(id);
        return true;
    }
}

package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account createAccount(CreateAccountDTO accountDTO){
        if(accountRepository.existsByUsername(accountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }

        Account account = new Account();
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setEmail(accountDTO.getEmail());
        account.setRole(accountDTO.getRole());

        return accountRepository.save(account);
    }

    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElse(null);
    }

    public Account getAccountByUsername(String username){
        return accountRepository.findByUsername(username).orElse(null);
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findByEmail(email).orElse(null);
    }

    public void updateAccount(Long accountID, UpdateAccountDTO accountDTO){
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
    }

    public void deleteAccount(Account account){
        accountRepository.delete(account);
    }

    public void deleteAccountById(Long id){
        Account account = accountRepository.findById(id).orElse(null);

        if(account == null){
            throw new RuntimeException("Account not found!");
        }

        accountRepository.deleteById(id);
    }
}

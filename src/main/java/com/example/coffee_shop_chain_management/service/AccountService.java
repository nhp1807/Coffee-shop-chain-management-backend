package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account){
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElse(null);
    }

    public Account getAccountByUsername(String username){
        return accountRepository.findByUsername(username).get();
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findByEmail(email).get();
    }

    public Account updateAccount(Account account){
        return accountRepository.save(account);
    }

    public void deleteAccount(Account account){
        accountRepository.delete(account);
    }

    public void deleteAccountById(Long id){
        accountRepository.deleteById(id);
    }
}

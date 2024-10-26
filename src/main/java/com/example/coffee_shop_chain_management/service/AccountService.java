package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.mapper.AccountMapper;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class AccountService {
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private PasswordEncoder passwordEncoder;

    public Account createAccount(CreateAccountDTO accountDTO){
        if(accountRepository.existsByUsername(accountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }

        Account account = accountMapper.toAccount(accountDTO);
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

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

    public void updateAccount(Account account){
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

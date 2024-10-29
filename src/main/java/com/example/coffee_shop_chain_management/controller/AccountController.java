package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.service.AccountService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/get/all")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/get/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("/create")
    public Account createAccount(@RequestBody CreateAccountDTO account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/update/{id}")
    public Account updateAccount(@RequestBody UpdateAccountDTO account, @PathVariable Long id) {
        return accountService.updateAccount(id, account);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccountById(id);
    }
}

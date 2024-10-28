package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.OTP;
import com.example.coffee_shop_chain_management.enums.OTPType;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.OTPRepository;
import com.example.coffee_shop_chain_management.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@lombok.extern.slf4j.Slf4j
@RestController
@RequestMapping("/api")
public class RegistrationController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register/account")
    @ResponseBody
    public Account createAccount(@RequestBody CreateAccountDTO account) {
        return accountService.createAccount(account);
    }

    @PostMapping("/update/account/{id}")
    public void updateAccount(@RequestBody UpdateAccountDTO account, @PathVariable Long id) {
        accountService.updateAccount(id, account);
    }
}

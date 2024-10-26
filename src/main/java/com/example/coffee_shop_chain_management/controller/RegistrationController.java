package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.OTP;
import com.example.coffee_shop_chain_management.enums.OTPType;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.OTPRepository;
import com.example.coffee_shop_chain_management.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register/account")
    public Account createAccount(@RequestBody CreateAccountDTO account) {
        return accountService.createAccount(account);
    }
}

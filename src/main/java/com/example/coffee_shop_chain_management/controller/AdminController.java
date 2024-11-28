package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AccountController accountController;

    @Autowired
    public AdminController(AccountController accountController) {
        this.accountController = accountController;
    }

    @GetMapping("/accounts")
    public String getAllAccounts(Model model) {
        model.addAttribute("accounts", accountController.getAllAccounts());
        return "admin/accounts";
    }

    @GetMapping("/account/{id}")
    public String getAccountById(Model model, @PathVariable Long id) {
        model.addAttribute("account", accountController.getAccountById(id));
        return "admin/account";
    }

    @GetMapping("/account/create")
    public String createAccountForm(Model model) {
        model.addAttribute("account", new CreateAccountDTO());
        return "admin/create_account";
    }

    @PostMapping("/account/create")
    public String createAccount(Model model, @ModelAttribute CreateAccountDTO account) {
        model.addAttribute("account", accountController.createAccount(account));
        return "redirect:/admin/accounts";
    }

    @PostMapping("/account/update/{id}")
    public String updateAccount(Model model, @ModelAttribute UpdateAccountDTO account, @PathVariable Long id) {
        model.addAttribute("account", accountController.updateAccount(account, id));
        return "redirect:/admin/accounts";
    }

    @PostMapping("/account/delete/{id}")
    public String deleteAccount(Model model, @PathVariable Long id) {
        model.addAttribute("account", accountController.deleteAccount(id));
        return "redirect:/admin/accounts";
    }
}

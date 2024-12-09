package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.AccountResponse;
import com.example.coffee_shop_chain_management.response.AccountStatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SendOTP sendOTP;
    @Value("${telegram.bot.link}")
    private String TELEGRAM_BOT_LINK;

    public APIResponse<List<AccountResponse>> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();

        return new APIResponse<>(accounts.stream().map(this::toAccountResponse).toList(), "Accounts retrieved successfully!", true);
    }

    @Transactional
    public APIResponse<AccountResponse> createAccount(CreateAccountDTO accountDTO){
        if(accountRepository.existsByUsername(accountDTO.getUsername())){
            return new APIResponse<>(null, "Account already exists!", false);
        }

        Branch branch =  branchRepository.findById(accountDTO.getBranchID()).get();
        // Neu co tai khoan co branch da ton tai thi khong tao tai khoan moi
        if (accountRepository.existsByBranch_BranchID(branch.getBranchID())) {
            return new APIResponse<>(null, "This branch has another account! ", false);
        }

        Account account = new Account();
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoder.encode("1234"));
        account.setRole("MANAGER");
        StringBuilder message = new StringBuilder();
        message.append("Your account is ").append("\n");
        message.append("Username: ").append(accountDTO.getUsername()).append("\n");
        message.append("Password: ").append("1234").append("\n");
        message.append("Click here to verify with telegram: ").append(TELEGRAM_BOT_LINK).append(accountDTO.getUsername());
        boolean sendStatus = sendOTP.sendMail(message.toString() ,accountDTO.getEmail());

        if (!sendStatus) {
            return new APIResponse<>(null, "Send email failed!", false);
        }

        account.setEmail(accountDTO.getEmail());
        account.setBranch(branch);

        Account newAccount = accountRepository.save(account);

        return new APIResponse<>(toAccountResponse(newAccount), "Account created successfully!", true);
    }

    public APIResponse<AccountResponse> getAccountById(Long id){
        Optional<Account> account = accountRepository.findById(id);

        if (!account.isPresent()) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        return new APIResponse<>(toAccountResponse(account.get()), "Account retrieved successfully!", true);
    }

    public APIResponse<AccountResponse> getAccountByUsername(String username){
        Optional<Account> account = accountRepository.findByUsername(username);

        if (!account.isPresent()) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        return new APIResponse<>(toAccountResponse(account.get()), "Account retrieved successfully!", true);
    }

    public APIResponse<AccountResponse> getAccountByEmail(String email){
        Optional<Account> account = accountRepository.findByEmail(email);

        if (!account.isPresent()) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        return new APIResponse<>(toAccountResponse(account.get()), "Account retrieved successfully!", true);
    }

    @Transactional
    public APIResponse<AccountResponse> updateAccount(Long accountID, UpdateAccountDTO accountDTO){
        Optional<Account> accountExisted = accountRepository.findById(accountID);

        if (!accountExisted.isPresent()) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        Account account = accountExisted.get();

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

    @Transactional
    public APIResponse<AccountResponse> deleteAccount(Account account){
        if (!accountRepository.existsById(account.getAccountID())) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        if (account.getRole().equals("ADMIN")) {
            return new APIResponse<>(null, "Cannot delete admin account!", false);
        }

        accountRepository.delete(account);
        return new APIResponse<>(null, "Account deleted successfully!", true);
    }

    @Transactional
    public APIResponse<AccountResponse> deleteAccountById(Long id){
        if (!accountRepository.existsById(id)) {
            return new APIResponse<>(null, "Account not found!", false);
        }

        if (accountRepository.findById(id).get().getRole().equals("ADMIN")) {
            return new APIResponse<>(null, "Cannot delete admin account!", false);
        }

        accountRepository.deleteById(id);
        return new APIResponse<>(null, "Account deleted successfully!", true);
    }

    public APIResponse<AccountStatResponse> getAccountStat(){
        List<Account> accounts = accountRepository.findAll();
        int totalAccounts = accounts.size();

        return new APIResponse<>(new AccountStatResponse(totalAccounts), "Account statistics retrieved successfully!", true);
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

package com.example.coffee_shop_chain_management.security;

import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        String username = authentication.getName();
        Optional<Account> accountExisted = accountRepository.findByUsername(username);

        if (accountExisted.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Account account = accountExisted.get();

        JSONObject responseBody = new JSONObject();
        responseBody.put("username", username);
        responseBody.put("role", authentication.getAuthorities().toArray()[0]);
        responseBody.put("status", "success");

        if (isAdmin) {
            responseBody.put("redirectUrl", "/admin/home");
        } else if (isManager) {
            responseBody.put("branchID", account.getBranch().getBranchID());
            responseBody.put("redirectUrl", "/manager/home");
        } else if (isEmployee) {
            responseBody.put("redirectUrl", "/employee/home");
            responseBody.put("branchID", account.getBranch().getBranchID());
        } else {
            responseBody.put("redirectUrl", "/home");
        }

        // Trả về phản hồi dưới dạng JSON
        response.setContentType("application/json");
        response.getWriter().write(responseBody.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}



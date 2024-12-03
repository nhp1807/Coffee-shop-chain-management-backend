package com.example.coffee_shop_chain_management.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        JSONObject responseBodyJson = new JSONObject();

        // Kiểm tra nếu lỗi là do tài khoản không đúng
        if (exception instanceof BadCredentialsException) {
            responseBodyJson.put("status", "error");
            responseBodyJson.put("message", "Invalid credentials");
        } else {
            responseBodyJson.put("status", "error");
            responseBodyJson.put("message", "Authentication failed");
        }

        // Trả về phản hồi dưới dạng JSON
        response.setContentType("application/json");
        response.getWriter().write(responseBodyJson.toString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}


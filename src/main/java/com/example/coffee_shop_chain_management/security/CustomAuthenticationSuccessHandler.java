package com.example.coffee_shop_chain_management.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        // Chuẩn bị URL chuyển hướng tùy theo vai trò
        JSONObject responseBodyJson = new JSONObject();
        if (isAdmin) {
            responseBodyJson.put("redirectUrl", "/admin/home");
            responseBodyJson.put("username", authentication.getName());
            responseBodyJson.put("role", authentication.getAuthorities().toArray()[0]);
            responseBodyJson.put("status", "success");
        } else if (isManager) {
            responseBodyJson.put("redirectUrl", "/manager/home");
            responseBodyJson.put("username", authentication.getName());
            responseBodyJson.put("role", authentication.getAuthorities().toArray()[0]);
            responseBodyJson.put("status", "success");
        } else if (isEmployee) {
            responseBodyJson.put("redirectUrl", "/employee/home");
            responseBodyJson.put("username", authentication.getName());
            responseBodyJson.put("role", authentication.getAuthorities().toArray()[0]);
            responseBodyJson.put("status", "success");
        } else {
            // Nếu không phải là admin, manager hoặc employee thì chuyển hướng về trang chủ
            responseBodyJson.put("redirectUrl", "/home");
            responseBodyJson.put("username", authentication.getName());
            responseBodyJson.put("role", authentication.getAuthorities().toArray()[0]);
            responseBodyJson.put("status", "success");
        }

        // Trả về phản hồi dưới dạng JSON
        response.setContentType("application/json");
        response.getWriter().write(responseBodyJson.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}



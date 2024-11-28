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

        // Chuẩn bị URL chuyển hướng tùy theo vai trò
//        Map<String, Object> responseBody = new HashMap<>();
        JSONObject responseBodyJson = new JSONObject();
        if (isAdmin) {
//            responseBody.put("redirectUrl", "/admin/home");
            responseBodyJson.put("redirectUrl", "/admin/home");
            responseBodyJson.put("role", "admin");
        } else if (isManager) {
//            responseBody.put("redirectUrl", "/manager/home");
            responseBodyJson.put("redirectUrl", "/manager/home");
            responseBodyJson.put("role", "manager");
        } else {
//            responseBody.put("redirectUrl", "/home");
            responseBodyJson.put("redirectUrl", "/home");
            responseBodyJson.put("role", "user");
        }

        // Trả về phản hồi dưới dạng JSON
        response.setContentType("application/json");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().write(responseBodyJson.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}



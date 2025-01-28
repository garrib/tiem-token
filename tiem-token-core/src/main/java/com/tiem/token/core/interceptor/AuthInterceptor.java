package com.tiem.token.core.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    private final TokenManager tokenManager;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查是否需要登录
        CheckLogin checkLogin = handlerMethod.getMethodAnnotation(CheckLogin.class);
        if (checkLogin != null) {
            tokenManager.checkLogin();
        }

        // 检查角色
        CheckRole checkRole = handlerMethod.getMethodAnnotation(CheckRole.class);
        if (checkRole != null) {
            for (String role : checkRole.value()) {
                tokenManager.checkRole(role);
            }
        }

        // 检查权限
        CheckPermission checkPermission = handlerMethod.getMethodAnnotation(CheckPermission.class);
        if (checkPermission != null) {
            for (String permission : checkPermission.value()) {
                tokenManager.checkPermission(permission);
            }
        }

        return true;
    }
} 
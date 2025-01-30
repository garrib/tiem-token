package com.tiem.token.test.middleware;

import com.tiem.token.core.middleware.AuthMiddleware;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingMiddleware implements AuthMiddleware {
    
    @Override
    public boolean beforeAuth(HttpServletRequest request, HttpServletResponse response) {
        log.info("Before auth: {}", request.getRequestURI());
        return true;
    }
    
    @Override
    public void afterAuth(HttpServletRequest request, HttpServletResponse response) {
        log.info("After auth: {}", request.getRequestURI());
    }
    
    @Override
    public int getOrder() {
        return 0;
    }
} 
package com.tiem.token.core.log;

import com.tiem.token.core.config.TTokenProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenLogger {
    
    private static final Logger log = LoggerFactory.getLogger("tiem.token");
    
    private final TTokenProperties properties;
    
    /**
     * 记录登录日志
     */
    public void logLogin(String userId, String token, boolean success, String message) {
        if (properties.isLog()) {
            if (success) {
                log.info("User login success - userId: {}, token: {}", userId, token);
            } else {
                log.warn("User login failed - userId: {}, reason: {}", userId, message);
            }
        }
    }
    
    /**
     * 记录未登录访问日志
     */
    public void logUnauthorized(String path) {
        if (properties.isLog()) {
            log.warn("Unauthorized access - path: {}", path);
        }
    }
    
    /**
     * 记录权限不足日志
     */
    public void logAccessDenied(String userId, String path, String requiredPermission) {
        if (properties.isLog()) {
            log.warn("Access denied - userId: {}, path: {}, required permission: {}", 
                userId, path, requiredPermission);
        }
    }
    
    /**
     * 记录角色不足日志
     */
    public void logRoleDenied(String userId, String path, String requiredRole) {
        if (properties.isLog()) {
            log.warn("Role denied - userId: {}, path: {}, required role: {}", 
                userId, path, requiredRole);
        }
    }
    
    /**
     * 记录登出日志
     */
    public void logLogout(String userId, String token) {
        if (properties.isLog()) {
            log.info("User logout - userId: {}, token: {}", userId, token);
        }
    }
} 
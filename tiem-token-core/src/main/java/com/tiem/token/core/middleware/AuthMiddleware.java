package com.tiem.token.core.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthMiddleware {
    /**
     * 在鉴权之前执行
     * @return true继续执行，false中断执行
     */
    default boolean beforeAuth(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }
    
    /**
     * 在鉴权之后执行
     */
    default void afterAuth(HttpServletRequest request, HttpServletResponse response) {
    }
    
    /**
     * 获取中间件执行顺序，数字越小越先执行
     */
    default int getOrder() {
        return 0;
    }
} 
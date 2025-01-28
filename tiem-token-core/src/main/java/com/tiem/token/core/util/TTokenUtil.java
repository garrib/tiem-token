package com.tiem.token.core.util;

import com.tiem.token.common.enums.BaseEnum;
import com.tiem.token.common.exception.AuthException;
import com.tiem.token.core.auth.TokenManager;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Token工具类，提供静态方法访问TokenManager的功能
 */
@UtilityClass
public class TTokenUtil {
    
    private static TokenManager tokenManager;
    
    /**
     * 初始化器，用于注入TokenManager
     */
    @Component
    public static class TokenManagerInjector implements ApplicationContextAware {
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            tokenManager = applicationContext.getBean(TokenManager.class);
        }
    }
    
    /**
     * 登录，创建token并关联用户对象
     */
    public static String login(Object userObj) {
        return tokenManager.createToken(userObj);
    }
    
    /**
     * 登出，移除当前token
     */
    public static void logout() {
        tokenManager.removeToken();
    }
    
    /**
     * 获取当前token
     */
    public static String getToken() {
        return tokenManager.getToken();
    }
    
    /**
     * 获取当前登录用户
     */
    public static Object getLoginUser() {
        return tokenManager.getLoginUser();
    }
    
    /**
     * 获取当前登录用户并转换为指定类型
     */
    public static <T> T getLoginUser(Class<T> userClass) {
        return tokenManager.getLoginUser(userClass);
    }
    
    /**
     * 检查是否已登录
     */
    public static void checkLogin() {
        tokenManager.checkLogin();
    }
    
    /**
     * 判断是否已登录
     */
    public static boolean isLogin() {
        try {
            checkLogin();
            return true;
        } catch (AuthException e) {
            return false;
        }
    }
    
    /**
     * 检查是否有指定角色（字符串方式）
     */
    public static boolean hasRole(String role) {
        return tokenManager.hasRole(role);
    }
    
    /**
     * 检查是否有指定角色（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> boolean hasRole(T role) {
        return tokenManager.hasRole(role);
    }
    
    /**
     * 检查是否有指定权限（字符串方式）
     */
    public static boolean hasPermission(String permission) {
        return tokenManager.hasPermission(permission);
    }
    
    /**
     * 检查是否有指定权限（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> boolean hasPermission(T permission) {
        return tokenManager.hasPermission(permission);
    }
    
    /**
     * 检查角色权限（字符串方式）
     */
    public static void checkRole(String role) {
        tokenManager.checkRole(role);
    }
    
    /**
     * 检查角色权限（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> void checkRole(T role) {
        tokenManager.checkRole(role);
    }
    
    /**
     * 检查操作权限（字符串方式）
     */
    public static void checkPermission(String permission) {
        tokenManager.checkPermission(permission);
    }
    
    /**
     * 检查操作权限（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> void checkPermission(T permission) {
        tokenManager.checkPermission(permission);
    }
} 
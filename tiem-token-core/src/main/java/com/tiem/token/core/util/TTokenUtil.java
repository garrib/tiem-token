package com.tiem.token.core.util;

import com.alibaba.fastjson2.JSON;
import com.tiem.token.common.enums.BaseEnum;
import com.tiem.token.common.exception.AuthException;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.context.TokenContext;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Token工具类，提供静态方法访问TokenManager的功能
 */
@UtilityClass
public class TTokenUtil {
    
    /**
     * 获取TokenManager
     */
    private static TokenManager getTokenManager() {
        return TokenContext.getTokenManager();
    }
    
    /**
     * 登录并创建token
     */
    public static <T> String login(T userObj) {
        return getTokenManager().createToken(userObj);
    }
    
    /**
     * 登出
     */
    public static void logout() {
        getTokenManager().removeToken();
    }
    
    /**
     * 获取当前token
     */
    public static String getToken() {
        return getTokenManager().getToken();
    }
    
    /**
     * 获取当前登录用户，自动推断类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T getLoginUser() {
        // 直接返回TokenManager中的对象，保持原始类型
        return getTokenManager().getLoginUser();
    }
    
    /**
     * 获取当前登录用户，指定类型
     * @deprecated 使用无参数的getLoginUser()方法代替
     */
    @Deprecated
    public static <T> T getLoginUser(Class<T> userClass) {
        return getTokenManager().getLoginUser(userClass);
    }
    
    /**
     * 检查是否已登录
     */
    public static boolean isLogin() {
        return getTokenManager().getLoginUser() != null;
    }
    
    /**
     * 检查是否有指定角色
     */
    public static boolean hasRole(String role) {
        return getTokenManager().hasRole(role);
    }
    
    /**
     * 检查是否有指定角色（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> boolean hasRole(T role) {
        return getTokenManager().hasRole(role);
    }
    
    /**
     * 检查是否有指定权限
     */
    public static boolean hasPermission(String permission) {
        return getTokenManager().hasPermission(permission);
    }
    
    /**
     * 检查是否有指定权限（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> boolean hasPermission(T permission) {
        return getTokenManager().hasPermission(permission);
    }
    
    /**
     * 检查角色权限（字符串方式）
     */
    public static void checkRole(String role) {
        getTokenManager().checkRole(role);
    }
    
    /**
     * 检查角色权限（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> void checkRole(T role) {
        getTokenManager().checkRole(role);
    }
    
    /**
     * 检查操作权限（字符串方式）
     */
    public static void checkPermission(String permission) {
        getTokenManager().checkPermission(permission);
    }
    
    /**
     * 检查操作权限（枚举方式）
     */
    public static <T extends Enum<T> & BaseEnum> void checkPermission(T permission) {
        getTokenManager().checkPermission(permission);
    }
} 
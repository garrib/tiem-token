package com.tiem.token.common.model;

import java.util.List;

/**
 * 登录用户接口
 */
public interface TLoginUser {
    /**
     * 获取用户ID
     */
    String getUserId();
    
    /**
     * 获取用户名
     */
    String getUsername();
    
    /**
     * 获取角色列表
     */
    List<String> getRoles();
    
    /**
     * 获取权限列表
     */
    List<String> getPermissions();
} 
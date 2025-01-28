package com.tiem.token.common.model;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.ArrayList;

/**
 * 默认登录用户实现
 */
@Data
@Accessors(chain = true)
public class TDefaultLoginUser implements TLoginUser {
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 角色列表
     */
    private List<String> roles = new ArrayList<>();
    
    /**
     * 权限列表
     */
    private List<String> permissions = new ArrayList<>();
} 
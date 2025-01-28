package com.tiem.token.core.config;

import com.tiem.token.core.config.getter.PermissionGetter;
import com.tiem.token.core.config.getter.RoleGetter;
import com.tiem.token.core.config.getter.UserIdGetter;
import com.tiem.token.core.generator.DefaultTokenGenerator;
import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.core.store.MemoryTokenStore;
import com.tiem.token.core.store.TokenStore;
import com.tiem.token.common.generator.TokenGenerator;
import com.tiem.token.common.model.TLoginUser;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Token配置抽象类，使用者可以继承此类来自定义配置
 */
public abstract class TTokenConfiguration {
    
    /**
     * 配置Token存储方式
     * 默认使用内存存储
     */
    public TokenStore tokenStore() {
        return new MemoryTokenStore();
    }
    
    /**
     * 配置Token生成方式
     * 默认使用UUID
     */
    public TokenGenerator tokenGenerator() {
        return new DefaultTokenGenerator();
    }
    
    /**
     * 配置用户ID获取方式
     * 默认从TLoginUser接口获取
     */
    public UserIdGetter userIdGetter() {
        return user -> {
            if (user instanceof TLoginUser) {
                return ((TLoginUser) user).getUserId();
            }
            return null;
        };
    }
    
    /**
     * 配置角色获取方式
     * 默认从TLoginUser接口获取
     */
    public RoleGetter roleGetter() {
        return user -> {
            if (user instanceof TLoginUser) {
                return ((TLoginUser) user).getRoles();
            }
            return null;
        };
    }
    
    /**
     * 配置权限获取方式
     * 默认从TLoginUser接口获取
     */
    public PermissionGetter permissionGetter() {
        return user -> {
            if (user instanceof TLoginUser) {
                return ((TLoginUser) user).getPermissions();
            }
            return null;
        };
    }
    
    /**
     * 配置自定义注解处理器
     * 默认为空
     */
    public List<AnnotationHandler<? extends Annotation>> customAnnotationHandlers() {
        return null;
    }
} 
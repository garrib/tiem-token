package com.tiem.token.core.config;

import com.tiem.token.common.constant.TokenConstant;
import com.tiem.token.common.enums.TokenStoreTypeEnum;
import com.tiem.token.core.config.getter.PermissionGetter;
import com.tiem.token.core.config.getter.RoleGetter;
import com.tiem.token.core.config.getter.UserIdGetter;
import com.tiem.token.core.generator.DefaultTokenGenerator;
import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.core.store.MemoryTokenStore;
import com.tiem.token.core.store.TokenStore;
import com.tiem.token.common.generator.TokenGenerator;
import com.tiem.token.common.model.TLoginUser;
import lombok.Getter;
import com.tiem.token.common.enums.TokenStorageEnum;
import java.util.Arrays;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Token配置类，使用builder模式配置
 */
@Getter
public class TTokenConfiguration {
    
    private TokenStore tokenStore;
    private TokenGenerator tokenGenerator;
    private UserIdGetter userIdGetter;
    private RoleGetter roleGetter;
    private PermissionGetter permissionGetter;
    private List<AnnotationHandler<? extends Annotation>> annotationHandlers;
    
    private String tokenName;
    private List<TokenStorageEnum> tokenStorage;
    private String tokenPrefix;
    private int cookieMaxAge;
    private String cookiePath;
    private String cookieDomain;
    private boolean cookieHttpOnly;
    private TokenStoreTypeEnum storeType;
    private long tokenExpireTime;
    
    private TTokenConfiguration() {
        // 设置默认值
        this.tokenStore = new MemoryTokenStore();
        this.tokenGenerator = new DefaultTokenGenerator();
        this.userIdGetter = defaultUserIdGetter();
        this.roleGetter = defaultRoleGetter();
        this.permissionGetter = defaultPermissionGetter();
        this.annotationHandlers = new ArrayList<>();
        
        // 设置TTokenProperties的默认值
        this.tokenName = TokenConstant.DEFAULT_TOKEN_NAME;
        this.tokenStorage = Arrays.asList(TokenStorageEnum.values());
        this.tokenPrefix = TokenConstant.DEFAULT_TOKEN_PREFIX;
        this.cookieMaxAge = TokenConstant.DEFAULT_COOKIE_MAX_AGE;
        this.cookiePath = TokenConstant.DEFAULT_COOKIE_PATH;
        this.cookieHttpOnly = TokenConstant.DEFAULT_COOKIE_HTTP_ONLY;
        this.storeType = TokenStoreTypeEnum.MEMORY;
        this.tokenExpireTime = TokenConstant.DEFAULT_TOKEN_EXPIRE_TIME;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    private UserIdGetter defaultUserIdGetter() {
        return user -> {
            if (user instanceof TLoginUser) {
                return ((TLoginUser) user).getUserId();
            }
            return null;
        };
    }
    
    private RoleGetter defaultRoleGetter() {
        return user -> {
            if (user instanceof TLoginUser) {
                return ((TLoginUser) user).getRoles();
            }
            return null;
        };
    }
    
    private PermissionGetter defaultPermissionGetter() {
        return user -> {
            if (user instanceof TLoginUser) {
                return ((TLoginUser) user).getPermissions();
            }
            return null;
        };
    }
    
    public static class Builder {
        private final TTokenConfiguration config;
        
        private Builder() {
            config = new TTokenConfiguration();
        }
        
        public Builder tokenStore(TokenStore tokenStore) {
            config.tokenStore = tokenStore;
            return this;
        }
        
        public Builder tokenGenerator(TokenGenerator tokenGenerator) {
            config.tokenGenerator = tokenGenerator;
            return this;
        }
        
        public Builder userIdGetter(UserIdGetter userIdGetter) {
            config.userIdGetter = userIdGetter;
            return this;
        }
        
        public Builder roleGetter(RoleGetter roleGetter) {
            config.roleGetter = roleGetter;
            return this;
        }
        
        public Builder permissionGetter(PermissionGetter permissionGetter) {
            config.permissionGetter = permissionGetter;
            return this;
        }
        
        public Builder addAnnotationHandler(AnnotationHandler<? extends Annotation> handler) {
            config.annotationHandlers.add(handler);
            return this;
        }
        
        public Builder annotationHandlers(List<AnnotationHandler<? extends Annotation>> handlers) {
            config.annotationHandlers = handlers;
            return this;
        }
        
        public Builder tokenName(String tokenName) {
            config.tokenName = tokenName;
            return this;
        }
        
        public Builder tokenStorage(List<TokenStorageEnum> tokenStorage) {
            config.tokenStorage = tokenStorage;
            return this;
        }
        
        public Builder tokenPrefix(String tokenPrefix) {
            config.tokenPrefix = tokenPrefix;
            return this;
        }
        
        public Builder cookieMaxAge(int cookieMaxAge) {
            config.cookieMaxAge = cookieMaxAge;
            return this;
        }
        
        public Builder cookiePath(String cookiePath) {
            config.cookiePath = cookiePath;
            return this;
        }
        
        public Builder cookieDomain(String cookieDomain) {
            config.cookieDomain = cookieDomain;
            return this;
        }
        
        public Builder cookieHttpOnly(boolean cookieHttpOnly) {
            config.cookieHttpOnly = cookieHttpOnly;
            return this;
        }
        
        public Builder storeType(TokenStoreTypeEnum storeType) {
            config.storeType = storeType;
            return this;
        }
        
        public Builder tokenExpireTime(long tokenExpireTime) {
            config.tokenExpireTime = tokenExpireTime;
            return this;
        }
        
        public TTokenConfiguration build() {
            return config;
        }
    }
} 
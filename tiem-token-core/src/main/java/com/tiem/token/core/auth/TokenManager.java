package com.tiem.token.core.auth;

import com.tiem.token.common.enums.BaseEnum;
import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.exception.AuthException;
import com.tiem.token.core.config.DefaultTTokenConfiguration;
import com.tiem.token.core.config.TTokenProperties;
import com.tiem.token.core.config.TTokenConfiguration;
import com.tiem.token.core.config.getter.PermissionGetter;
import com.tiem.token.core.config.getter.RoleGetter;
import com.tiem.token.core.config.getter.UserIdGetter;
import com.tiem.token.core.store.TokenStore;
import com.tiem.token.common.generator.TokenGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import com.tiem.token.core.log.TokenLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static com.tiem.token.common.constant.TokenConstant.*;

@Slf4j
@Component
public class TokenManager {
    
    private static final Map<String, Object> tokenUserMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
    
    @Autowired
    private TTokenProperties properties;
    
    @Autowired
    @Qualifier("tokenConfiguration")
    private TTokenConfiguration configuration;
    
    @Autowired(required = false)
    @Qualifier("defaultTokenConfiguration")
    private DefaultTTokenConfiguration defaultConfiguration;
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private HttpServletResponse response;
    
    @Autowired
    private TokenLogger tokenLogger;
    
    private TokenStore tokenStore;
    private TokenGenerator tokenGenerator;
    private UserIdGetter userIdGetter;
    private RoleGetter roleGetter;
    private PermissionGetter permissionGetter;
    
    @PostConstruct
    public void init() {
        // 优先使用用户配置，如果没有则使用默认配置
        TTokenConfiguration effectiveConfig = configuration != null ? configuration : defaultConfiguration;
        
        this.tokenStore = effectiveConfig.getTokenStore();
        this.tokenGenerator = effectiveConfig.getTokenGenerator();
        this.userIdGetter = effectiveConfig.getUserIdGetter();
        this.roleGetter = effectiveConfig.getRoleGetter();
        this.permissionGetter = effectiveConfig.getPermissionGetter();
    }
    
    private String getTokenName() {
        return configuration.getTokenName() != null ? 
               configuration.getTokenName() : 
               properties.getTokenName();
    }
    
    private List<TokenStorageEnum> getTokenStorage() {
        return !configuration.getTokenStorage().isEmpty() ? 
               configuration.getTokenStorage() : 
               properties.getTokenStorage();
    }
    
    private String getTokenPrefix() {
        return configuration.getTokenPrefix() != null ? 
               configuration.getTokenPrefix() : 
               properties.getTokenPrefix();
    }
    
    private TokenStore getTokenStore() {
        return configuration.getTokenStore();
    }
    
    private TokenGenerator getTokenGenerator() {
        return configuration.getTokenGenerator();
    }
    
    private UserIdGetter getUserIdGetter() {
        return configuration.getUserIdGetter();
    }
    
    private RoleGetter getRoleGetter() {
        return configuration.getRoleGetter();
    }
    
    private PermissionGetter getPermissionGetter() {
        return configuration.getPermissionGetter();
    }
    
    /**
     * 创建token并关联用户对象
     */
    public String createToken(Object userObj) {
        String userId = getUserId(userObj);
        TokenStore store = getTokenStore();
        
        if (userId != null) {
            String existingToken = store.getTokenByUserId(userId);
            
            // 如果已经存在token
            if (existingToken != null) {
                // 如果允许并发登录且共享token，直接返回已存在的token
                if (properties.isConcurrent() && properties.isShare()) {
                    return existingToken;
                }
                
                // 如果不允许并发登录，移除旧token
                if (!properties.isConcurrent()) {
                    store.remove(existingToken);
                    store.removeUserIdToken(userId);
                }
            }
        }
        
        // 创建新token
        String token = getTokenGenerator().generate(userObj);
        store.store(token, userObj);
        
        // 使用配置的存储方式
        if (getTokenStorage().contains(TokenStorageEnum.COOKIE)) {
            Cookie cookie = new Cookie(getTokenName(), token);
            cookie.setPath(configuration.getCookiePath());
            cookie.setMaxAge(configuration.getCookieMaxAge());
            if (StringUtils.hasText(configuration.getCookieDomain())) {
                cookie.setDomain(configuration.getCookieDomain());
            }
            cookie.setHttpOnly(configuration.isCookieHttpOnly());
            response.addCookie(cookie);
        }
        
        tokenLogger.logLogin(userId, token, true, null);
        return token;
    }
    
    /**
     * 获取当前token
     */
    public String getToken() {
        String token = tokenHolder.get();
        if (token != null) {
            return token;
        }

        // 按照配置的顺序依次尝试获取token
        for (TokenStorageEnum storage : getTokenStorage()) {
            token = getTokenFromStorage(storage);
            if (token != null) {
                break;
            }
        }
        
        if (token != null) {
            tokenHolder.set(token);
        }
        return token;
    }
    
    private String getTokenFromStorage(TokenStorageEnum storage) {
        String token = null;
        switch (storage) {
            case HEADER:
                String headerToken = request.getHeader(getTokenName());
                if (StringUtils.hasText(headerToken)) {
                    if (StringUtils.hasText(getTokenPrefix()) 
                            && headerToken.startsWith(getTokenPrefix())) {
                        token = headerToken.substring(getTokenPrefix().length());
                    } else {
                        token = headerToken;
                    }
                }
                break;
            case COOKIE:
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (getTokenName().equals(cookie.getName())) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }
                break;
        }
        return token;
    }
    
    /**
     * 检查token是否过期
     */
    private boolean isTokenExpired(String token) {
        TokenStore store = getTokenStore();
        Long createTime = store.getCreateTime(token);
        Long lastAccessTime = store.getLastAccessTime(token);
        
        if (createTime == null || lastAccessTime == null) {
            return true;
        }
        
        long now = System.currentTimeMillis();
        
        // 检查绝对过期时间
        if (now - createTime > properties.getTimeout() * 1000) {
            return true;
        }
        
        // 检查临时过期时间
        if (now - lastAccessTime > properties.getActiveTimeout() * 1000) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 获取当前登录用户
     */
    @SuppressWarnings("unchecked")
    public <T> T getLoginUser() {
        String token = getToken();
        if (token != null) {
            // 检查token是否过期
            if (isTokenExpired(token)) {
                removeToken();
                return null;
            }
            
            // 更新最后访问时间
            getTokenStore().updateLastAccessTime(token);
            
            return (T) getTokenStore().getUser(token, Object.class);
        }
        return null;
    }
    
    /**
     * 获取当前登录用户并转换为指定类型
     * @deprecated 使用无参数的getLoginUser()方法代替
     */
    @Deprecated
    public <T> T getLoginUser(Class<T> userClass) {
        return getLoginUser();
    }
    
    /**
     * 移除token
     */
    public void removeToken() {
        String token = getToken();
        if (token != null) {
            Object userObj = getLoginUser();
            String userId = getUserId(userObj);
            
            getTokenStore().remove(token);
            tokenHolder.remove();
            
            if (getTokenStorage().contains(TokenStorageEnum.COOKIE)) {
                Cookie cookie = new Cookie(getTokenName(), null);
                cookie.setPath(configuration.getCookiePath());
                cookie.setMaxAge(0);
                if (StringUtils.hasText(configuration.getCookieDomain())) {
                    cookie.setDomain(configuration.getCookieDomain());
                }
                response.addCookie(cookie);
            }
            
            tokenLogger.logLogout(userId, token);
        }
    }
    
    public void checkLogin() {
        if (getToken() == null) {
            tokenLogger.logUnauthorized(request.getRequestURI());
            throw new AuthException(ERROR_NOT_LOGIN);
        }
    }
    
    /**
     * 检查是否有指定角色
     */
    public boolean hasRole(String role) {
        Object userObj = getLoginUser();
        if (userObj == null) {
            return false;
        }
        return getUserRoles(userObj).contains(role);
    }
    
    /**
     * 检查是否有指定角色(枚举方式)
     */
    public <T extends Enum<T> & BaseEnum> boolean hasRole(T role) {
        return hasRole(role.getCode());
    }
    
    /**
     * 检查是否有指定权限
     */
    public boolean hasPermission(String permission) {
        Object userObj = getLoginUser();
        if (userObj == null) {
            return false;
        }
        return getUserPermissions(userObj).contains(permission);
    }
    
    /**
     * 检查是否有指定权限(枚举方式)
     */
    public <T extends Enum<T> & BaseEnum> boolean hasPermission(T permission) {
        return hasPermission(permission.getCode());
    }
    
    /**
     * 检查角色权限
     */
    public void checkRole(String role) {
        if (!hasRole(role)) {
            throw new AuthException(ERROR_NO_ROLE);
        }
    }
    
    /**
     * 检查角色权限(枚举方式)
     */
    public <T extends Enum<T> & BaseEnum> void checkRole(T role) {
        if (!hasRole(role)) {
            throw new AuthException(ERROR_NO_ROLE);
        }
    }
    
    /**
     * 检查操作权限
     */
    public void checkPermission(String permission) {
        if (!hasPermission(permission)) {
            throw new AuthException(ERROR_NO_PERMISSION);
        }
    }
    
    /**
     * 检查操作权限(枚举方式)
     */
    public <T extends Enum<T> & BaseEnum> void checkPermission(T permission) {
        if (!hasPermission(permission)) {
            throw new AuthException(ERROR_NO_PERMISSION);
        }
    }
    
    /**
     * 获取用户角色列表
     */
    private List<String> getUserRoles(Object userObj) {
        RoleGetter roleGetter = getRoleGetter();
        if (roleGetter != null) {
            try {
                List<String> roles = roleGetter.getRoles(userObj);
                return roles != null ? roles : new ArrayList<>();
            } catch (Exception e) {
                log.error("Failed to get user roles", e);
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取用户权限列表
     */
    private List<String> getUserPermissions(Object userObj) {
        PermissionGetter permissionGetter = getPermissionGetter();
        if (permissionGetter != null) {
            try {
                List<String> permissions = permissionGetter.getPermissions(userObj);
                return permissions != null ? permissions : new ArrayList<>();
            } catch (Exception e) {
                log.error("Failed to get user permissions", e);
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
    
    private String getUserId(Object userObj) {
        if (userObj == null) {
            return null;
        }
        UserIdGetter getter = getUserIdGetter();
        if (getter != null) {
            return getter.getUserId(userObj);
        }
        return null;
    }
} 
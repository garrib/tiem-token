package com.tiem.token.core.auth;

import com.tiem.token.common.enums.BaseEnum;
import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.exception.AuthException;
import com.tiem.token.core.config.TTokenProperties;
import com.tiem.token.core.config.TTokenConfiguration;
import com.tiem.token.core.config.getter.PermissionGetter;
import com.tiem.token.core.config.getter.RoleGetter;
import com.tiem.token.core.config.getter.UserIdGetter;
import com.tiem.token.core.store.TokenStore;
import com.tiem.token.common.generator.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.tiem.token.common.model.TLoginUser;
import java.util.List;
import java.util.ArrayList;

@Component
public class TokenManager {
    
    private static final Map<String, Object> tokenUserMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
    
    private static final String ERROR_NOT_LOGIN = "未登录";
    private static final String ERROR_NO_ROLE = "没有所需角色权限";
    private static final String ERROR_NO_PERMISSION = "没有所需操作权限";
    
    private final TTokenProperties properties;
    private final TTokenConfiguration configuration;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
    private final TokenStore tokenStore;
    private final TokenGenerator tokenGenerator;
    private final UserIdGetter userIdGetter;
    private final RoleGetter roleGetter;
    private final PermissionGetter permissionGetter;
    
    public TokenManager(TTokenProperties properties,
                       TTokenConfiguration configuration,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        this.properties = properties;
        this.configuration = configuration;
        this.request = request;
        this.response = response;
        
        this.tokenStore = configuration.getTokenStore();
        this.tokenGenerator = configuration.getTokenGenerator();
        this.userIdGetter = configuration.getUserIdGetter();
        this.roleGetter = configuration.getRoleGetter();
        this.permissionGetter = configuration.getPermissionGetter();
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
        String token = getTokenGenerator().generate(userObj);
        getTokenStore().store(token, userObj);
        
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
     * 获取当前登录用户
     */
    public Object getLoginUser() {
        String token = getToken();
        if (token != null) {
            return getTokenStore().getUser(token, Object.class);
        }
        return null;
    }
    
    /**
     * 获取当前登录用户并转换为指定类型
     */
    public <T> T getLoginUser(Class<T> userClass) {
        Object userObj = getLoginUser();
        if (userObj != null && userClass.isInstance(userObj)) {
            return userClass.cast(userObj);
        }
        return null;
    }
    
    /**
     * 移除token
     */
    public void removeToken() {
        String token = getToken();
        if (token != null) {
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
        }
    }
    
    public void checkLogin() {
        if (getToken() == null) {
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
        if (userObj instanceof TLoginUser) {
            return ((TLoginUser) userObj).getRoles();
        }
        try {
            // 向后兼容，通过反射获取roles属性
            Object result = userObj.getClass().getMethod("getRoles").invoke(userObj);
            if (result instanceof List) {
                return (List<String>) result;
            } else if (result instanceof String[]) {
                return Arrays.asList((String[]) result);
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取用户权限列表
     */
    private List<String> getUserPermissions(Object userObj) {
        if (userObj instanceof TLoginUser) {
            return ((TLoginUser) userObj).getPermissions();
        }
        try {
            // 向后兼容，通过反射获取permissions属性
            Object result = userObj.getClass().getMethod("getPermissions").invoke(userObj);
            if (result instanceof List) {
                return (List<String>) result;
            } else if (result instanceof String[]) {
                return Arrays.asList((String[]) result);
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return new ArrayList<>();
    }
} 
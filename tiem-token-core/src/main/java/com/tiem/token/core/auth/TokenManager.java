package com.tiem.token.core.auth;

import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.core.config.TTokenProperties;
import com.tiem.token.core.store.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenManager {
    
    private static final Map<String, Object> tokenUserMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
    
    private static final String ERROR_NOT_LOGIN = "未登录";
    private static final String ERROR_NO_ROLE = "没有所需角色权限";
    private static final String ERROR_NO_PERMISSION = "没有所需操作权限";
    
    private final TTokenProperties properties;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final TokenStore tokenStore;
    
    public TokenManager(TTokenProperties properties, 
                       HttpServletRequest request, 
                       HttpServletResponse response,
                       TokenStore tokenStore) {
        this.properties = properties;
        this.request = request;
        this.response = response;
        this.tokenStore = tokenStore;
    }

    /**
     * 创建token并关联用户对象
     */
    public String createToken(Object userObj) {
        String token = generateToken();
        tokenStore.store(token, userObj);
        
        // 根据配置存储token
        if (properties.getTokenStorage().contains(TokenStorageEnum.COOKIE)) {
            Cookie cookie = new Cookie(properties.getTokenName(), token);
            cookie.setPath(properties.getCookiePath());
            cookie.setMaxAge(properties.getCookieMaxAge());
            if (StringUtils.hasText(properties.getCookieDomain())) {
                cookie.setDomain(properties.getCookieDomain());
            }
            cookie.setHttpOnly(properties.isCookieHttpOnly());
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
        for (TokenStorageEnum storage : properties.getTokenStorage()) {
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
                String headerToken = request.getHeader(properties.getTokenName());
                if (StringUtils.hasText(headerToken)) {
                    if (StringUtils.hasText(properties.getTokenPrefix()) 
                            && headerToken.startsWith(properties.getTokenPrefix())) {
                        token = headerToken.substring(properties.getTokenPrefix().length());
                    } else {
                        token = headerToken;
                    }
                }
                break;
            case COOKIE:
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (properties.getTokenName().equals(cookie.getName())) {
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
     * 获取token关联的用户对象
     */
    public <T> T getLoginUser(Class<T> userClass) {
        String token = getToken();
        if (token != null) {
            return tokenStore.getUser(token, userClass);
        }
        return null;
    }
    
    /**
     * 移除token
     */
    public void removeToken() {
        String token = getToken();
        if (token != null) {
            tokenStore.remove(token);
            tokenHolder.remove();
            
            if (properties.getTokenStorage().contains(TokenStorageEnum.COOKIE)) {
                Cookie cookie = new Cookie(properties.getTokenName(), null);
                cookie.setPath(properties.getCookiePath());
                cookie.setMaxAge(0);
                if (StringUtils.hasText(properties.getCookieDomain())) {
                    cookie.setDomain(properties.getCookieDomain());
                }
                response.addCookie(cookie);
            }
        }
    }
    
    private String generateToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public void checkLogin() {
        if (getToken() == null) {
            throw new AuthException(ERROR_NOT_LOGIN);
        }
    }
    
    public void checkRole(String role) {
        if (!hasRole(role)) {
            throw new AuthException(ERROR_NO_ROLE);
        }
    }
    
    public void checkPermission(String permission) {
        if (!hasPermission(permission)) {
            throw new AuthException(ERROR_NO_PERMISSION);
        }
    }
} 
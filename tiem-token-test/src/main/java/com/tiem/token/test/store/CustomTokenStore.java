package com.tiem.token.test.store;

import com.tiem.token.core.store.TokenStore;
import com.tiem.token.common.exception.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义Token存储实现示例
 */
@Slf4j
public class CustomTokenStore implements TokenStore {
    
    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void store(String token, Object userObj) {
        try {
            store.put(token, userObj);
        } catch (Exception e) {
            log.error("Failed to store token", e);
            throw new AuthException("Failed to store token");
        }
    }
    
    @Override
    public <T> T getUser(String token, Class<T> userClass) {
        try {
            Object userObj = store.get(token);
            if (userObj == null) {
                return null;
            }
            // 使用ObjectMapper进行类型转换
            return objectMapper.convertValue(userObj, userClass);
        } catch (Exception e) {
            log.error("Failed to get user", e);
            throw new AuthException("Failed to get user");
        }
    }
    
    @Override
    public void remove(String token) {
        try {
            store.remove(token);
        } catch (Exception e) {
            log.error("Failed to remove token", e);
            throw new AuthException("Failed to remove token");
        }
    }
    
    @Override
    public boolean exists(String token) {
        try {
            return store.containsKey(token);
        } catch (Exception e) {
            log.error("Failed to check token existence", e);
            throw new AuthException("Failed to check token existence");
        }
    }
} 
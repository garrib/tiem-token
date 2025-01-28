package com.tiem.token.test.store;

import com.tiem.token.core.store.TokenStore;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义Token存储实现示例
 */
public class CustomTokenStore implements TokenStore {
    
    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void store(String token, Object userObj) {
        store.put(token, userObj);
    }
    
    @Override
    public <T> T getUser(String token, Class<T> userClass) {
        Object userObj = store.get(token);
        if (userObj == null) {
            return null;
        }
        // 使用ObjectMapper进行类型转换
        return objectMapper.convertValue(userObj, userClass);
    }
    
    @Override
    public void remove(String token) {
        store.remove(token);
    }
} 
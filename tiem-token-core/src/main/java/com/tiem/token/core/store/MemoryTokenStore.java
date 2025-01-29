package com.tiem.token.core.store;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoryTokenStore implements TokenStore {
    
    // 存储原始对象，不做转换
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    @Override
    public void store(String token, Object userObj) {
        // 直接存储原始对象
        store.put(token, userObj);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getUser(String token, Class<T> userClass) {
        Object userObj = store.get(token);
        if (userClass.isInstance(userObj)) {
            return (T) userObj;
        }
        return null;
    }
    
    @Override
    public void remove(String token) {
        store.remove(token);
    }
    
    @Override
    public boolean exists(String token) {
        return store.containsKey(token);
    }
} 
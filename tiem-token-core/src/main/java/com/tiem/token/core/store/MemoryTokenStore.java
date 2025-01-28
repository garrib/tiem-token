package com.tiem.token.core.store;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoryTokenStore implements TokenStore {
    
    private static final Map<String, Object> tokenUserMap = new ConcurrentHashMap<>();
    
    @Override
    public void store(String token, Object userObj) {
        tokenUserMap.put(token, userObj);
    }
    
    @Override
    public <T> T getUser(String token, Class<T> userClass) {
        Object userObj = tokenUserMap.get(token);
        if (userObj != null && userClass.isInstance(userObj)) {
            return userClass.cast(userObj);
        }
        return null;
    }
    
    @Override
    public void remove(String token) {
        tokenUserMap.remove(token);
    }
    
    @Override
    public boolean exists(String token) {
        return tokenUserMap.containsKey(token);
    }
} 
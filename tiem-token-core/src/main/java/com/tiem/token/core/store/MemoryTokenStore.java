package com.tiem.token.core.store;

import com.tiem.token.common.model.TLoginUser;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tiem.token.common.constant.TokenConstant.*;

@Component
@ConditionalOnProperty(
    prefix = CONFIG_PREFIX,
    name = PROP_STORE_TYPE,
    havingValue = STORE_TYPE_MEMORY,
    matchIfMissing = true  // 如果属性未配置，默认使用内存存储
)
public class MemoryTokenStore implements TokenStore {
    
    @Data
    private static class TokenInfo {
        private final Object userObj;
        private final long createTime;
        private long lastAccessTime;
        private final String userId;
        
        public TokenInfo(Object userObj, String userId) {
            this.userObj = userObj;
            this.userId = userId;
            this.createTime = System.currentTimeMillis();
            this.lastAccessTime = this.createTime;
        }
    }
    
    private final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();
    private final Map<String, String> userIdTokenMap = new ConcurrentHashMap<>();
    
    @Override
    public void store(String token, Object userObj) {
        String userId = getUserId(userObj);
        TokenInfo info = new TokenInfo(userObj, userId);
        tokenStore.put(token, info);
        if (userId != null) {
            storeUserIdToken(userId, token);
        }
    }
    
    @Override
    public String getTokenByUserId(String userId) {
        return userIdTokenMap.get(userId);
    }
    
    @Override
    public void storeUserIdToken(String userId, String token) {
        userIdTokenMap.put(userId, token);
    }
    
    @Override
    public void removeUserIdToken(String userId) {
        userIdTokenMap.remove(userId);
    }
    
    private String getUserId(Object userObj) {
        if (userObj instanceof TLoginUser) {
            return ((TLoginUser) userObj).getUserId();
        }
        return null;
    }
    
    @Override
    public void updateLastAccessTime(String token) {
        TokenInfo info = tokenStore.get(token);
        if (info != null) {
            info.setLastAccessTime(System.currentTimeMillis());
        }
    }
    
    @Override
    public Long getLastAccessTime(String token) {
        TokenInfo info = tokenStore.get(token);
        return info != null ? info.getLastAccessTime() : null;
    }
    
    @Override
    public Long getCreateTime(String token) {
        TokenInfo info = tokenStore.get(token);
        return info != null ? info.getCreateTime() : null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getUser(String token, Class<T> userClass) {
        TokenInfo info = tokenStore.get(token);
        if (info != null && userClass.isInstance(info.getUserObj())) {
            return (T) info.getUserObj();
        }
        return null;
    }
    
    @Override
    public void remove(String token) {
        tokenStore.remove(token);
    }
    
    @Override
    public boolean exists(String token) {
        return tokenStore.containsKey(token);
    }
} 
package com.tiem.token.core.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiem.token.core.config.TTokenProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import com.tiem.token.common.constant.TokenConstant;

@Component
@ConditionalOnClass(StringRedisTemplate.class)
public class RedisTokenStore implements TokenStore {
    
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final TTokenProperties properties;
    
    public RedisTokenStore(StringRedisTemplate redisTemplate, 
                          ObjectMapper objectMapper,
                          TTokenProperties properties) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }
    
    @Override
    public void store(String token, Object userObj) {
        try {
            String userJson = objectMapper.writeValueAsString(userObj);
            String key = getRedisKey(token);
            redisTemplate.opsForValue().set(key, userJson, 
                    properties.getTokenExpireTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(TokenConstant.ERROR_STORE_TOKEN, e);
        }
    }
    
    @Override
    public <T> T getUser(String token, Class<T> userClass) {
        try {
            String key = getRedisKey(token);
            String userJson = redisTemplate.opsForValue().get(key);
            if (userJson != null) {
                return objectMapper.readValue(userJson, userClass);
            }
        } catch (Exception e) {
            throw new RuntimeException(TokenConstant.ERROR_GET_USER, e);
        }
        return null;
    }
    
    @Override
    public void remove(String token) {
        String key = getRedisKey(token);
        redisTemplate.delete(key);
    }
    
    @Override
    public boolean exists(String token) {
        String key = getRedisKey(token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    private String getRedisKey(String token) {
        return TokenConstant.REDIS_TOKEN_KEY_PREFIX + token;
    }
} 
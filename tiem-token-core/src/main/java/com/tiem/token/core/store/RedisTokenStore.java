package com.tiem.token.core.store;

import com.alibaba.fastjson2.JSON;
import com.tiem.token.core.config.TTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.tiem.token.common.constant.TokenConstant.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
    prefix = CONFIG_PREFIX,
    name = PROP_STORE_TYPE,
    havingValue = STORE_TYPE_REDIS
)
public class RedisTokenStore implements TokenStore {
    
    private final StringRedisTemplate redisTemplate;
    private final TTokenProperties properties;
    
    @Override
    public void store(String token, Object userObj) {
        String userKey = REDIS_USER_PREFIX + token;
        String timeKey = REDIS_TIME_PREFIX + token;
        long now = System.currentTimeMillis();
        
        // 存储用户对象
        redisTemplate.opsForValue().set(
            userKey, 
            JSON.toJSONString(userObj),
            properties.getTimeout(),
            TimeUnit.SECONDS
        );
        
        // 存储时间信息
        redisTemplate.opsForValue().set(
            timeKey,
            now + "," + now,
            properties.getTimeout(),
            TimeUnit.SECONDS
        );
        
        // 存储用户ID关联
        String userId = getUserId(userObj);
        if (userId != null) {
            storeUserIdToken(userId, token);
        }
    }
    
    @Override
    public String getTokenByUserId(String userId) {
        String key = REDIS_USER_ID_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }
    
    @Override
    public void storeUserIdToken(String userId, String token) {
        String key = REDIS_USER_ID_PREFIX + userId;
        redisTemplate.opsForValue().set(
            key,
            token,
            properties.getTimeout(),
            TimeUnit.SECONDS
        );
    }
    
    @Override
    public void removeUserIdToken(String userId) {
        String key = REDIS_USER_ID_PREFIX + userId;
        redisTemplate.delete(key);
    }
    
    private String getUserId(Object userObj) {
        if (userObj instanceof com.tiem.token.common.model.TLoginUser) {
            return ((com.tiem.token.common.model.TLoginUser) userObj).getUserId();
        }
        return null;
    }
    
    @Override
    public void updateLastAccessTime(String token) {
        String timeKey = REDIS_TIME_PREFIX + token;
        String timeStr = redisTemplate.opsForValue().get(timeKey);
        if (timeStr != null) {
            String createTime = timeStr.split(",")[0];
            String newTimeStr = createTime + "," + System.currentTimeMillis();
            redisTemplate.opsForValue().set(
                timeKey,
                newTimeStr,
                properties.getTimeout(),
                TimeUnit.SECONDS
            );
        }
    }
    
    @Override
    public Long getLastAccessTime(String token) {
        String timeKey = REDIS_TIME_PREFIX + token;
        String timeStr = redisTemplate.opsForValue().get(timeKey);
        if (timeStr != null) {
            String[] times = timeStr.split(",");
            return Long.parseLong(times[1]);
        }
        return null;
    }
    
    @Override
    public Long getCreateTime(String token) {
        String timeKey = REDIS_TIME_PREFIX + token;
        String timeStr = redisTemplate.opsForValue().get(timeKey);
        if (timeStr != null) {
            String[] times = timeStr.split(",");
            return Long.parseLong(times[0]);
        }
        return null;
    }
    
    @Override
    public <T> T getUser(String token, Class<T> userClass) {
        String userKey = REDIS_USER_PREFIX + token;
        String json = redisTemplate.opsForValue().get(userKey);
        if (json != null) {
            return JSON.parseObject(json, userClass);
        }
        return null;
    }
    
    @Override
    public void remove(String token) {
        String userKey = REDIS_USER_PREFIX + token;
        String timeKey = REDIS_TIME_PREFIX + token;
        redisTemplate.delete(userKey);
        redisTemplate.delete(timeKey);
    }
    
    @Override
    public boolean exists(String token) {
        String userKey = REDIS_USER_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(userKey));
    }
} 
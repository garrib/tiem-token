package com.tiem.token.core.config;

import com.tiem.token.core.store.TokenStore;
import com.tiem.token.core.store.MemoryTokenStore;
import com.tiem.token.core.store.RedisTokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TokenStoreConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "tiem.token.store-type", havingValue = "memory", matchIfMissing = true)
    public TokenStore memoryTokenStore() {
        return new MemoryTokenStore();
    }
    
    @Bean
    @ConditionalOnProperty(name = "tiem.token.store-type", havingValue = "redis")
    public TokenStore redisTokenStore(StringRedisTemplate redisTemplate, 
                                    ObjectMapper objectMapper,
                                    TTokenProperties properties) {
        return new RedisTokenStore(redisTemplate, objectMapper, properties);
    }
} 
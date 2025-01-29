package com.tiem.token.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiem.token.common.enums.TokenStoreTypeEnum;
import com.tiem.token.core.store.MemoryTokenStore;
import com.tiem.token.core.store.RedisTokenStore;
import com.tiem.token.core.store.TokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * TokenStore配置类
 */
@Configuration
public class TokenStoreConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "tiem.token.store-type", havingValue = "redis")
    public TokenStore redisTokenStore(StringRedisTemplate redisTemplate,
                                    ObjectMapper objectMapper,
                                    TTokenProperties properties) {
        return new RedisTokenStore(redisTemplate, objectMapper, properties);
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "tiem.token.store-type", havingValue = "memory", matchIfMissing = true)
    public TokenStore memoryTokenStore() {
        return new MemoryTokenStore();
    }
} 
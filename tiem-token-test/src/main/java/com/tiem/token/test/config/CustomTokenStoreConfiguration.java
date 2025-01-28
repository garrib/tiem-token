package com.tiem.token.test.config;

import com.tiem.token.core.store.TokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tiem.token.test.store.CustomTokenStore;

/**
 * 自定义Token存储配置示例
 */
@Configuration
public class CustomTokenStoreConfiguration {
    
    /**
     * 自定义Token存储实现
     */
    @Bean
    public TokenStore tokenStore() {
        return new CustomTokenStore();
    }
} 
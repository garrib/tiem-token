package com.tiem.token.core.config;

import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.impl.CheckLoginHandler;
import com.tiem.token.core.handler.impl.CheckPermissionHandler;
import com.tiem.token.core.handler.impl.CheckRoleHandler;
import com.tiem.token.core.store.TokenStore;
import com.tiem.token.core.store.MemoryTokenStore;
import com.tiem.token.core.store.RedisTokenStore;
import com.tiem.token.core.interceptor.AuthInterceptor;
import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.core.generator.DefaultTokenGenerator;
import com.tiem.token.common.generator.TokenGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Token自动配置类
 */
@Configuration
@ConditionalOnProperty(prefix = "tiem.token", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(TTokenProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class TTokenAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TTokenConfiguration tokenConfiguration(TTokenProperties properties) {
        return TTokenConfiguration.builder()
            .tokenName(properties.getTokenName())
            .tokenStorage(properties.getTokenStorage())
            .tokenPrefix(properties.getTokenPrefix())
            .cookieMaxAge(properties.getCookieMaxAge())
            .cookiePath(properties.getCookiePath())
            .cookieDomain(properties.getCookieDomain())
            .cookieHttpOnly(properties.isCookieHttpOnly())
            .storeType(properties.getStoreType())
            .tokenExpireTime(properties.getTokenExpireTime())
            .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenManager tokenManager(TTokenProperties properties,
                                   TTokenConfiguration configuration,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        return new TokenManager(properties, configuration, request, response);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthInterceptor authInterceptor(TokenManager tokenManager,
                                         TTokenConfiguration configuration) {
        List<AnnotationHandler<? extends Annotation>> handlers = new ArrayList<>();
        // 添加默认处理器
        handlers.add(new CheckLoginHandler());
        handlers.add(new CheckRoleHandler());
        handlers.add(new CheckPermissionHandler());
        
        // 添加自定义处理器
        if (configuration.getAnnotationHandlers() != null) {
            handlers.addAll(configuration.getAnnotationHandlers());
        }
        
        return new AuthInterceptor(tokenManager, handlers);
    }
    
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
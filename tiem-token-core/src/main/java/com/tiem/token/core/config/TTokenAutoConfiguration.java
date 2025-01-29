package com.tiem.token.core.config;

import com.tiem.token.common.enums.TokenStoreTypeEnum;
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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Token自动配置类
 * 只在用户没有自定义配置时生效
 */
@Configuration
@AutoConfigureAfter({RedisAutoConfiguration.class, TokenStoreConfiguration.class})
@ConditionalOnMissingBean(type = "com.tiem.token.test.config.TestTokenConfiguration")
public class TTokenAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TTokenConfiguration.class)
    public TTokenConfiguration tokenConfiguration(TTokenProperties properties, TokenStore tokenStore) {
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
            .tokenStore(tokenStore)
            .build();
    }

    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager(TTokenProperties properties,
                                   TTokenConfiguration configuration,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        return new TokenManager(properties, configuration, request, response);
    }

    @Bean
    @ConditionalOnMissingBean(AuthInterceptor.class)
    public AuthInterceptor authInterceptor(TokenManager tokenManager,
                                         TTokenConfiguration configuration) {
        List<AnnotationHandler<? extends Annotation>> handlers = new ArrayList<>();
        handlers.add(new CheckLoginHandler());
        handlers.add(new CheckRoleHandler());
        handlers.add(new CheckPermissionHandler());
        
        if (configuration.getAnnotationHandlers() != null) {
            handlers.addAll(configuration.getAnnotationHandlers());
        }
        
        return new AuthInterceptor(tokenManager, handlers);
    }
} 
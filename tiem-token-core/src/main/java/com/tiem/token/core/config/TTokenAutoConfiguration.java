package com.tiem.token.core.config;

import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.impl.CheckLoginHandler;
import com.tiem.token.core.handler.impl.CheckPermissionHandler;
import com.tiem.token.core.handler.impl.CheckRoleHandler;
import com.tiem.token.core.store.TokenStore;
import com.tiem.token.core.interceptor.AuthInterceptor;
import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.core.log.TokenLogger;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import static com.tiem.token.common.constant.TokenConstant.CONFIG_PREFIX;
import static com.tiem.token.common.constant.TokenConstant.PROP_ENABLED;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Token自动配置类
 * 只在用户没有自定义配置时生效
 */
@Configuration
@ConditionalOnProperty(prefix = CONFIG_PREFIX, name = PROP_ENABLED, matchIfMissing = true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class TTokenAutoConfiguration {
    @Autowired
    TTokenProperties properties;

    @Autowired
    TokenStore tokenStore;

    @Bean("defaultTokenConfiguration")
    public DefaultTTokenConfiguration defaultTokenConfiguration() {
        return DefaultTTokenConfiguration.builder()
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
    public TokenManager tokenManager() {
        return new TokenManager();
    }

    @Bean
    @ConditionalOnMissingBean(AuthInterceptor.class)
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
} 
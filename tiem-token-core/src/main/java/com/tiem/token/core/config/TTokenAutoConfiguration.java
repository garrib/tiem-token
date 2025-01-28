package com.tiem.token.core.config;

import com.tiem.token.core.auth.TokenManager;
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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Token自动配置类
 */
@Configuration
@EnableConfigurationProperties(TTokenProperties.class)
public class TTokenAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TTokenConfiguration.class)
    public TTokenConfiguration defaultTokenConfiguration() {
        return new TTokenConfiguration() {};  // 使用默认实现
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(TTokenConfiguration configuration) {
        return configuration.tokenStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenGenerator tokenGenerator(TTokenConfiguration configuration) {
        return configuration.tokenGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenManager tokenManager(TTokenProperties properties,
                                   TokenStore tokenStore,
                                   TokenGenerator tokenGenerator,
                                   TTokenConfiguration configuration) {
        return new TokenManager(properties, tokenStore, tokenGenerator,
                configuration.userIdGetter(),
                configuration.roleGetter(),
                configuration.permissionGetter());
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
        if (configuration.customAnnotationHandlers() != null) {
            handlers.addAll(configuration.customAnnotationHandlers());
        }
        
        return new AuthInterceptor(tokenManager, handlers);
    }
} 
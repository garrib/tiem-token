package com.tiem.token.core.config;

import com.tiem.token.core.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.tiem.token.common.constant.TokenConstant.CONFIG_PREFIX;
import static com.tiem.token.common.constant.TokenConstant.PROP_ENABLED;

@Configuration
@ConditionalOnProperty(
    prefix = CONFIG_PREFIX,
    name = PROP_ENABLED,
    havingValue = "true",
    matchIfMissing = true
)
public class TTokenWebMvcConfigurer implements WebMvcConfigurer {
    
    @Autowired
    private AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**")  // 拦截所有请求
            .excludePathPatterns(    // 排除不需要拦截的路径
                "/error",
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs/**",
                "/swagger-ui.html/**"
            );
    }
} 
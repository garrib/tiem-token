package com.tiem.token.core.config;

import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.core.handler.impl.CheckLoginHandler;
import com.tiem.token.core.handler.impl.CheckRoleHandler;
import com.tiem.token.core.handler.impl.CheckPermissionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.List;

@Configuration
public class AuthHandlerConfiguration {
    
    /**
     * 注册默认的注解处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public List<AnnotationHandler<? extends Annotation>> defaultAnnotationHandlers() {
        return List.of(
            new CheckLoginHandler(),
            new CheckRoleHandler(),
            new CheckPermissionHandler()
        );
    }
} 
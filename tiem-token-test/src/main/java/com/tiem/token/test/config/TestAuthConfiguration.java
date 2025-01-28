package com.tiem.token.test.config;

import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.test.handler.CheckAdminHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestAuthConfiguration {
    
    /**
     * 添加自定义的注解处理器
     */
    @Bean
    public List<AnnotationHandler<? extends Annotation>> customAnnotationHandlers(
            List<AnnotationHandler<? extends Annotation>> defaultHandlers) {
        List<AnnotationHandler<? extends Annotation>> allHandlers = new ArrayList<>(defaultHandlers);
        // 添加自定义处理器
        allHandlers.add(new CheckAdminHandler());
        return allHandlers;
    }
} 
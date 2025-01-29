package com.tiem.token.core.handler.impl;

import com.tiem.token.common.enums.BaseEnum;
import com.tiem.token.common.exception.AuthException;
import com.tiem.token.core.annotation.CheckRole;
import com.tiem.token.core.annotation.Role;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.AnnotationHandler;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class CheckRoleHandler implements AnnotationHandler<Annotation> {
    
    @Override
    public void handle(Annotation annotation, Object tokenManager) {
        TokenManager manager = (TokenManager) tokenManager;
        
        // 处理原生CheckRole注解
        if (annotation instanceof CheckRole) {
            handleCheckRole((CheckRole) annotation, manager);
            return;
        }
        
        // 处理自定义角色注解
        Role role = annotation.annotationType().getAnnotation(Role.class);
        if (role != null) {
            handleCustomRole(annotation, role.value(), manager);
        }
    }
    
    private void handleCheckRole(CheckRole annotation, TokenManager manager) {
        for (String role : annotation.value()) {
            manager.checkRole(role);
        }
    }
    
    private void handleCustomRole(Annotation annotation, Class<? extends BaseEnum> enumClass, TokenManager manager) {
        try {
            // 获取注解的value方法
            Method valueMethod = annotation.annotationType().getDeclaredMethod("value");
            Object[] enumValues = (Object[]) valueMethod.invoke(annotation);
            
            // 检查每个枚举值的角色
            for (Object enumValue : enumValues) {
                if (enumValue instanceof BaseEnum) {
                    manager.checkRole(((BaseEnum) enumValue).getCode());
                }
            }
        } catch (Exception e) {
            throw new AuthException("Failed to process custom role annotation");
        }
    }
    
    @Override
    public Class<Annotation> getAnnotationType() {
        return Annotation.class;
    }
} 
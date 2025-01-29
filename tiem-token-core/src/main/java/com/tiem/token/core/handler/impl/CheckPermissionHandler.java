package com.tiem.token.core.handler.impl;

import com.tiem.token.common.enums.BaseEnum;
import com.tiem.token.common.exception.AuthException;
import com.tiem.token.core.annotation.CheckPermission;
import com.tiem.token.core.annotation.Permission;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.AnnotationHandler;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class CheckPermissionHandler implements AnnotationHandler<Annotation> {
    
    @Override
    public void handle(Annotation annotation, Object tokenManager) {
        TokenManager manager = (TokenManager) tokenManager;
        
        // 处理原生CheckPermission注解
        if (annotation instanceof CheckPermission) {
            handleCheckPermission((CheckPermission) annotation, manager);
            return;
        }
        
        // 处理自定义权限注解
        Permission permission = annotation.annotationType().getAnnotation(Permission.class);
        if (permission != null) {
            handleCustomPermission(annotation, permission.value(), manager);
        }
    }
    
    private void handleCheckPermission(CheckPermission annotation, TokenManager manager) {
        for (String permission : annotation.value()) {
            manager.checkPermission(permission);
        }
    }
    
    private void handleCustomPermission(Annotation annotation, Class<? extends BaseEnum> enumClass, TokenManager manager) {
        try {
            // 获取注解的value方法
            Method valueMethod = annotation.annotationType().getDeclaredMethod("value");
            Object[] enumValues = (Object[]) valueMethod.invoke(annotation);
            
            // 检查每个枚举值的权限
            for (Object enumValue : enumValues) {
                if (enumValue instanceof BaseEnum) {
                    manager.checkPermission(((BaseEnum) enumValue).getCode());
                }
            }
        } catch (Exception e) {
            throw new AuthException("Failed to process custom permission annotation");
        }
    }
    
    @Override
    public Class<Annotation> getAnnotationType() {
        return Annotation.class;
    }
} 
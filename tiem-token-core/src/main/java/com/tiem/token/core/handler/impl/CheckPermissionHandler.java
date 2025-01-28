package com.tiem.token.core.handler.impl;

import com.tiem.token.core.annotation.CheckPermission;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.AnnotationHandler;
import org.springframework.stereotype.Component;

@Component
public class CheckPermissionHandler implements AnnotationHandler<CheckPermission> {
    
    @Override
    public void handle(CheckPermission annotation, Object tokenManager) {
        for (String permission : annotation.value()) {
            ((TokenManager)tokenManager).checkPermission(permission);
        }
    }
    
    @Override
    public Class<CheckPermission> getAnnotationType() {
        return CheckPermission.class;
    }
} 
package com.tiem.token.core.handler.impl;

import com.tiem.token.common.enums.BaseEnum;
import com.tiem.token.core.annotation.CheckRole;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.AnnotationHandler;
import org.springframework.stereotype.Component;

@Component
public class CheckRoleHandler implements AnnotationHandler<CheckRole> {
    
    @Override
    public void handle(CheckRole annotation, Object tokenManager) {
        TokenManager manager = (TokenManager) tokenManager;
        
        for (String role : annotation.value()) {
            manager.checkRole(role);
        }
    }
    
    @Override
    public Class<CheckRole> getAnnotationType() {
        return CheckRole.class;
    }
} 
package com.tiem.token.test.handler;

import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.test.annotation.CheckAdmin;
import org.springframework.stereotype.Component;

@Component
public class CheckAdminHandler implements AnnotationHandler<CheckAdmin> {
    
    @Override
    public void handle(CheckAdmin annotation, Object tokenManager) {
        ((TokenManager)tokenManager).checkRole(annotation.value());
    }
    
    @Override
    public Class<CheckAdmin> getAnnotationType() {
        return CheckAdmin.class;
    }
} 
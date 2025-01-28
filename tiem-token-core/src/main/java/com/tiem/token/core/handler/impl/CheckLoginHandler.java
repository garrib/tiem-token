package com.tiem.token.core.handler.impl;

import com.tiem.token.core.annotation.CheckLogin;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.AnnotationHandler;
import org.springframework.stereotype.Component;

@Component
public class CheckLoginHandler implements AnnotationHandler<CheckLogin> {
    
    @Override
    public void handle(CheckLogin annotation, Object tokenManager) {
        ((TokenManager)tokenManager).checkLogin();
    }
    
    @Override
    public Class<CheckLogin> getAnnotationType() {
        return CheckLogin.class;
    }
} 
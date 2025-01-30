package com.tiem.token.core.context;

import com.tiem.token.core.auth.TokenManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Token上下文，用于获取TokenManager实例
 */
@Component
public class TokenContext implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    
    /**
     * 获取TokenManager实例
     */
    public static TokenManager getTokenManager() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext has not been initialized");
        }
        return applicationContext.getBean(TokenManager.class);
    }
} 
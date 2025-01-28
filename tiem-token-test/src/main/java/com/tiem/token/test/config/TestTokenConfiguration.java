package com.tiem.token.test.config;

import com.tiem.token.core.config.TTokenConfiguration;
import com.tiem.token.core.store.TokenStore;
import com.tiem.token.common.generator.TokenGenerator;
import com.tiem.token.common.model.TLoginUser;
import com.tiem.token.common.handler.AnnotationHandler;
import com.tiem.token.common.handler.CheckAdminHandler;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestTokenConfiguration extends TTokenConfiguration {
    
    @Override
    public TokenStore tokenStore() {
        return new CustomTokenStore();
    }
    
    @Override
    public TokenGenerator tokenGenerator() {
        return userObj -> {
            if (userObj instanceof TLoginUser) {
                return ((TLoginUser) userObj).getUserId() + "_" + System.currentTimeMillis();
            }
            return UUID.randomUUID().toString();
        };
    }
    
    @Override
    public UserIdGetter userIdGetter() {
        return user -> {
            if (user instanceof CustomUser) {
                return ((CustomUser) user).getId();
            }
            return null;
        };
    }
    
    @Override
    public List<AnnotationHandler<? extends Annotation>> customAnnotationHandlers() {
        return List.of(new CheckAdminHandler());
    }
} 
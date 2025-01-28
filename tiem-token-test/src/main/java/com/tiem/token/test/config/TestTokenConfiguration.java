package com.tiem.token.test.config;

import com.tiem.token.core.config.TTokenConfiguration;
import com.tiem.token.test.handler.CheckAdminHandler;
import com.tiem.token.test.store.CustomTokenStore;
import com.tiem.token.test.model.CustomUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.UUID;
import java.util.List;
import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.enums.TokenStoreTypeEnum;

@Configuration
public class TestTokenConfiguration {
    
    @Bean
    public TTokenConfiguration tokenConfiguration() {
        return TTokenConfiguration.builder()
            // 自定义存储实现
            .tokenStore(new CustomTokenStore())
            // 自定义token生成
            .tokenGenerator(userObj -> {
                if (userObj instanceof CustomUser) {
                    return ((CustomUser) userObj).getId() + "_" + System.currentTimeMillis();
                }
                return UUID.randomUUID().toString();
            })
            // 覆盖TTokenProperties的配置
            .tokenName("X-Custom-Token")
            .tokenPrefix("Custom ")
            .tokenStorage(List.of(TokenStorageEnum.HEADER))
            .cookieMaxAge(7200)
            .storeType(TokenStoreTypeEnum.REDIS)
            .tokenExpireTime(7200)
            // 自定义用户ID获取
            .userIdGetter(user -> {
                if (user instanceof CustomUser) {
                    return ((CustomUser) user).getId();
                }
                return null;
            })
            // 添加自定义注解处理器
            .addAnnotationHandler(new CheckAdminHandler())
            .build();
    }
} 
package com.tiem.token.test.config;

import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.enums.TokenStoreTypeEnum;
import com.tiem.token.core.config.TTokenConfiguration;
import com.tiem.token.test.handler.CheckAdminHandler;
import com.tiem.token.test.model.CustomUser;
import com.tiem.token.test.store.CustomTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

/**
 * 自定义Token配置示例
 */
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
            // 自定义角色获取
            .roleGetter(user -> {
                if (user instanceof CustomUser) {
                    return ((CustomUser) user).getRoles();
                }
                return null;
            })
            // 自定义权限获取
            .permissionGetter(user -> {
                if (user instanceof CustomUser) {
                    return ((CustomUser) user).getPermissions();
                }
                return null;
            })
            // 添加自定义注解处理器
            .addAnnotationHandler(new CheckAdminHandler())
            .build();
    }
} 
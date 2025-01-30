package com.tiem.token.test.config;

import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.enums.TokenStoreTypeEnum;
import com.tiem.token.core.config.DefaultTTokenConfiguration;
import com.tiem.token.core.config.TTokenConfiguration;
import com.tiem.token.test.handler.CheckAdminHandler;
import com.tiem.token.test.model.CustomUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.UUID;

/**
 * 自定义Token配置示例
 */
@Configuration
public class TestTokenConfiguration {

    @Bean("tokenConfiguration")
    @Primary
    public TTokenConfiguration tokenConfiguration(
            @Qualifier("defaultTokenConfiguration") DefaultTTokenConfiguration defaultConfig) {
        return TTokenConfiguration.builder()
                // 自定义token生成
                .tokenGenerator(userObj -> {
                    if (userObj instanceof CustomUser) {
                        return ((CustomUser) userObj).getId() + "_" + System.currentTimeMillis();
                    }
                    return UUID.randomUUID().toString();
                })
                // 使用默认配置的值
                .tokenName("iiib")
                // 覆盖需要修改的配置
                .tokenPrefix("Custom ")
                .build();
    }
} 
package com.tiem.token.core.config;

import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.enums.TokenStoreTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Token配置属性
 */
@Data
@Component  // 添加这个注解
@ConfigurationProperties(prefix = "tiem.token")
public class TTokenProperties {
    
    /**
     * 是否启用，默认true
     */
    private boolean enabled = true;

    /**
     * token名称
     */
    private String tokenName = "token";

    /**
     * token存储方式
     */
    private List<TokenStorageEnum> tokenStorage = new ArrayList<>() {{
        add(TokenStorageEnum.HEADER);
    }};

    /**
     * token前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * cookie过期时间（秒）
     */
    private int cookieMaxAge = -1;

    /**
     * cookie路径
     */
    private String cookiePath = "/";

    /**
     * cookie域名
     */
    private String cookieDomain;

    /**
     * cookie是否只允许http访问
     */
    private boolean cookieHttpOnly = true;

    /**
     * 存储类型
     */
    private TokenStoreTypeEnum storeType = TokenStoreTypeEnum.MEMORY;

    /**
     * token过期时间（秒），默认30分钟
     */
    private long tokenExpireTime = 1800;
} 
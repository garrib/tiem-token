package com.tiem.token.core.config;

import com.tiem.token.common.constant.TokenConstant;
import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.enums.TokenStoreTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "tiem.token")
public class TTokenProperties {
    
    /**
     * token名称，默认为Authorization
     */
    private String tokenName = TokenConstant.DEFAULT_TOKEN_NAME;
    
    /**
     * token存储位置：HEADER,COOKIE,ALL，默认ALL
     */
    private List<TokenStorageEnum> tokenStorage = Arrays.asList(TokenStorageEnum.values());
    
    /**
     * token前缀，默认Bearer，可以设置为空
     */
    private String tokenPrefix = TokenConstant.DEFAULT_TOKEN_PREFIX;
    
    /**
     * cookie过期时间，单位秒，默认24小时
     */
    private int cookieMaxAge = TokenConstant.DEFAULT_COOKIE_MAX_AGE;
    
    /**
     * cookie路径
     */
    private String cookiePath = TokenConstant.DEFAULT_COOKIE_PATH;
    
    /**
     * cookie域名
     */
    private String cookieDomain;
    
    /**
     * 是否启用httpOnly
     */
    private boolean cookieHttpOnly = TokenConstant.DEFAULT_COOKIE_HTTP_ONLY;
    
    /**
     * token存储类型：memory或redis，默认memory
     */
    private TokenStoreTypeEnum storeType = TokenStoreTypeEnum.MEMORY;
    
    /**
     * token过期时间，单位秒，默认24小时
     * 仅在redis存储模式下生效
     */
    private long tokenExpireTime = TokenConstant.DEFAULT_TOKEN_EXPIRE_TIME;
} 
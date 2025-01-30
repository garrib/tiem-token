package com.tiem.token.core.config;

import com.tiem.token.common.enums.TokenStorageEnum;
import com.tiem.token.common.enums.TokenStoreTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tiem.token.common.constant.TokenConstant.*;

/**
 * Token配置属性
 */
@Data
@Primary
@Component
@ConfigurationProperties(prefix = CONFIG_PREFIX)
public class TTokenProperties {
    
    /**
     * 是否启用，默认true
     */
    private boolean enabled = true;

    /**
     * token名称
     */
    private String tokenName = DEFAULT_TOKEN_NAME;

    /**
     * token存储方式
     */
    private List<TokenStorageEnum> tokenStorage = new ArrayList<>(Arrays.asList(TokenStorageEnum.values()));

    /**
     * token前缀
     */
    private String tokenPrefix = DEFAULT_TOKEN_PREFIX;

    /**
     * cookie过期时间（秒）
     */
    private int cookieMaxAge = DEFAULT_COOKIE_MAX_AGE;

    /**
     * cookie路径
     */
    private String cookiePath = DEFAULT_COOKIE_PATH;

    /**
     * cookie域名
     */
    private String cookieDomain;

    /**
     * cookie是否只允许http访问
     */
    private boolean cookieHttpOnly = DEFAULT_COOKIE_HTTP_ONLY;

    /**
     * 存储类型
     */
    private TokenStoreTypeEnum storeType = TokenStoreTypeEnum.MEMORY;

    /**
     * token过期时间（秒），默认30分钟
     */
    private long tokenExpireTime = DEFAULT_TOKEN_EXPIRE_TIME;

    /**
     * token有效期（必定过期）单位: 秒，默认一天
     */
    private long timeout = DEFAULT_TIMEOUT;

    /**
     * token临时有效期（指定时间无操作就过期）单位: 秒，默认30分钟
     */
    private long activeTimeout = DEFAULT_ACTIVE_TIMEOUT;

    /**
     * 是否允许同一账号并发登录，默认true
     */
    private boolean concurrent = true;

    /**
     * 是否共用token，默认true
     */
    private boolean share = true;

    /**
     * 是否输出操作日志，默认true
     */
    private boolean log = true;
} 
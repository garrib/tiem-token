package com.tiem.token.common.constant;

public interface TokenConstant {
    // 存储类型
    String STORE_TYPE_MEMORY = "MEMORY";
    String STORE_TYPE_REDIS = "REDIS";
    
    // 配置前缀
    String CONFIG_PREFIX = "tiem.token";
    
    // 配置属性名
    String PROP_ENABLED = "enabled";
    String PROP_STORE_TYPE = "store-type";
    String PROP_TOKEN_NAME = "token-name";
    String PROP_TOKEN_PREFIX = "token-prefix";
    String PROP_TOKEN_STORAGE = "token-storage";
    String PROP_COOKIE_MAX_AGE = "cookie-max-age";
    String PROP_COOKIE_PATH = "cookie-path";
    String PROP_COOKIE_DOMAIN = "cookie-domain";
    String PROP_COOKIE_HTTP_ONLY = "cookie-http-only";
    String PROP_TOKEN_EXPIRE_TIME = "token-expire-time";
    String PROP_TIMEOUT = "timeout";
    String PROP_ACTIVE_TIMEOUT = "active-timeout";
    String PROP_CONCURRENT = "concurrent";
    String PROP_SHARE = "share";
    String PROP_LOG = "log";
    
    // Token相关默认值
    String DEFAULT_TOKEN_NAME = "Authorization";
    String DEFAULT_TOKEN_PREFIX = "Bearer ";
    String DEFAULT_COOKIE_PATH = "/";
    int DEFAULT_COOKIE_MAX_AGE = -1;
    boolean DEFAULT_COOKIE_HTTP_ONLY = true;
    
    // 过期时间相关默认值（单位：秒）
    long DEFAULT_TOKEN_EXPIRE_TIME = 1800; // 30分钟
    long DEFAULT_TIMEOUT = 86400;          // 1天
    long DEFAULT_ACTIVE_TIMEOUT = 1800;    // 30分钟
    
    // Redis key前缀
    String REDIS_TOKEN_PREFIX = "token:";
    String REDIS_USER_PREFIX = "token:user:";
    String REDIS_TIME_PREFIX = "token:time:";
    String REDIS_USER_ID_PREFIX = "token:userid:";
    
    // 错误消息
    String ERROR_NOT_LOGIN = "未登录";
    String ERROR_NO_ROLE = "没有所需角色权限";
    String ERROR_NO_PERMISSION = "没有所需操作权限";
} 
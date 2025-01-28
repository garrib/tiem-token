package com.tiem.token.common.constant;

public interface TokenConstant {
    // Token相关默认值
    String DEFAULT_TOKEN_NAME = "Authorization";
    String DEFAULT_TOKEN_PREFIX = "Bearer ";
    int DEFAULT_COOKIE_MAX_AGE = 24 * 60 * 60;
    long DEFAULT_TOKEN_EXPIRE_TIME = 24 * 60 * 60;
    
    // Cookie相关默认值
    String DEFAULT_COOKIE_PATH = "/";
    boolean DEFAULT_COOKIE_HTTP_ONLY = true;
    
    // Redis相关默认值
    String DEFAULT_REDIS_HOST = "localhost";
    int DEFAULT_REDIS_PORT = 6379;
    int DEFAULT_REDIS_DATABASE = 0;
    
    // Redis key前缀
    String REDIS_TOKEN_KEY_PREFIX = "token:";
    
    // 错误消息
    String ERROR_STORE_TOKEN = "Failed to store token";
    String ERROR_GET_USER = "Failed to get user";
} 
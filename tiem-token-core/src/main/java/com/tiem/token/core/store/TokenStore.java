package com.tiem.token.core.store;

public interface TokenStore {
    /**
     * 存储token和用户对象的关联关系
     */
    void store(String token, Object userObj);
    
    /**
     * 获取token关联的用户对象
     */
    <T> T getUser(String token, Class<T> userClass);
    
    /**
     * 移除token
     */
    void remove(String token);
    
    /**
     * 判断token是否存在
     */
    boolean exists(String token);
} 
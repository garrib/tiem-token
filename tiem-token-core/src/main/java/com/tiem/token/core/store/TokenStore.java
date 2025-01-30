package com.tiem.token.core.store;

public interface TokenStore {
    /**
     * 存储token和用户对象的关联关系
     */
    void store(String token, Object userObj);
    
    /**
     * 更新最后访问时间
     */
    void updateLastAccessTime(String token);
    
    /**
     * 获取最后访问时间
     */
    Long getLastAccessTime(String token);
    
    /**
     * 获取token创建时间
     */
    Long getCreateTime(String token);
    
    /**
     * 根据token获取用户对象
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

    /**
     * 根据用户ID获取token
     */
    String getTokenByUserId(String userId);

    /**
     * 存储用户ID和token的关联关系
     */
    void storeUserIdToken(String userId, String token);

    /**
     * 移除用户ID关联的token
     */
    void removeUserIdToken(String userId);
} 
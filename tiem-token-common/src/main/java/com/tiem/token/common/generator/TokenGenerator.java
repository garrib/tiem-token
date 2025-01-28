package com.tiem.token.common.generator;

/**
 * Token生成器接口
 */
public interface TokenGenerator {
    /**
     * 生成token
     * @param userObj 用户对象
     * @return token字符串
     */
    String generate(Object userObj);
} 
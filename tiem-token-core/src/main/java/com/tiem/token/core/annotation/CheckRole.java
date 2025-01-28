package com.tiem.token.core.annotation;

import java.lang.annotation.*;

/**
 * 检查角色注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRole {
    /**
     * 需要的角色列表，有一个即可
     */
    String[] value();
} 
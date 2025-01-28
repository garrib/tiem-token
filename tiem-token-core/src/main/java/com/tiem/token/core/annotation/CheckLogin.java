package com.tiem.token.core.annotation;

import java.lang.annotation.*;

/**
 * 检查是否登录注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckLogin {
} 
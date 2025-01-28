package com.tiem.token.core.annotation;

import java.lang.annotation.*;

/**
 * 检查权限注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {
    /**
     * 需要的权限列表，需要全部满足
     * 支持字符串和枚举两种方式：
     * - 字符串：@CheckPermission("user:add")
     * - 枚举：@CheckPermission(PermissionEnum.USER_ADD.getCode())
     */
    String[] value() default {};
} 
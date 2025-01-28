package com.tiem.token.test.annotation;

import com.tiem.token.core.annotation.Permission;
import com.tiem.token.test.enums.PermissionEnum;
import java.lang.annotation.*;

/**
 * 用户权限注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Permission(PermissionEnum.class)
public @interface UserPermission {
    /**
     * 需要的用户权限列表
     */
    PermissionEnum[] value();
} 
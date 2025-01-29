package com.tiem.token.test.annotation;

import com.tiem.token.core.annotation.Role;
import com.tiem.token.test.enums.RoleEnum;
import java.lang.annotation.*;

/**
 * 用户角色注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Role(RoleEnum.class)
public @interface UserRole {
    /**
     * 需要的用户角色列表，有一个即可
     */
    RoleEnum[] value();
} 
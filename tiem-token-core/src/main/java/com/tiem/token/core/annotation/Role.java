package com.tiem.token.core.annotation;

import com.tiem.token.common.enums.BaseEnum;
import java.lang.annotation.*;

/**
 * 角色元注解，用于创建自定义角色注解
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Role {
    /**
     * 指定角色枚举类
     */
    Class<? extends BaseEnum> value();
} 
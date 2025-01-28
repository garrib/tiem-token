package com.tiem.token.core.annotation;

import com.tiem.token.common.enums.BaseEnum;
import java.lang.annotation.*;

/**
 * 权限元注解，用于创建自定义权限注解
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    /**
     * 指定权限枚举类
     */
    Class<? extends BaseEnum> value();
} 
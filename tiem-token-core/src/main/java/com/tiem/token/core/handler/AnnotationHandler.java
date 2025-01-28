package com.tiem.token.core.handler;

import java.lang.annotation.Annotation;

/**
 * 注解处理器接口
 * @param <A> 注解类型
 */
public interface AnnotationHandler<A extends Annotation> {
    
    /**
     * 处理注解
     * @param annotation 注解实例
     * @param tokenManager token管理器
     */
    void handle(A annotation, Object tokenManager);
    
    /**
     * 获取支持的注解类型
     */
    Class<A> getAnnotationType();
} 
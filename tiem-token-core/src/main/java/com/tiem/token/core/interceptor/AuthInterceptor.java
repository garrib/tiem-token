package com.tiem.token.core.interceptor;

import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.core.auth.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    
    private final TokenManager tokenManager;
    private final List<AnnotationHandler<? extends Annotation>> handlers;
    private final Map<Class<? extends Annotation>, AnnotationHandler<? extends Annotation>> handlerMap = new ConcurrentHashMap<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        // 初始化处理器映射
        if (handlerMap.isEmpty()) {
            initHandlerMap();
        }
        
        // 获取方法上的所有注解
        for (Annotation annotation : handlerMethod.getMethod().getAnnotations()) {
            AnnotationHandler<Annotation> annotationHandler = (AnnotationHandler<Annotation>) handlerMap.get(annotation.annotationType());
            if (annotationHandler != null) {
                annotationHandler.handle(annotation, tokenManager);
            }
        }

        return true;
    }
    
    private void initHandlerMap() {
        for (AnnotationHandler<? extends Annotation> handler : handlers) {
            handlerMap.put(handler.getAnnotationType(), handler);
        }
    }
} 
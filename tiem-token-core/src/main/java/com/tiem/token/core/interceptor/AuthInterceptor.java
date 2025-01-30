package com.tiem.token.core.interceptor;

import com.tiem.token.core.config.TTokenConfiguration;
import com.tiem.token.core.handler.AnnotationHandler;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.core.handler.impl.CheckLoginHandler;
import com.tiem.token.core.handler.impl.CheckPermissionHandler;
import com.tiem.token.core.handler.impl.CheckRoleHandler;
import com.tiem.token.core.middleware.AuthMiddleware;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private TokenManager tokenManager;
    
    @Autowired
    private TTokenConfiguration configuration;
    
    private List<AnnotationHandler<? extends Annotation>> handlers;
    
    @PostConstruct
    public void init() {
        handlers = new ArrayList<>();
        handlers.add(new CheckLoginHandler());
        handlers.add(new CheckRoleHandler());
        handlers.add(new CheckPermissionHandler());
        
        if (configuration.getAnnotationHandlers() != null) {
            handlers.addAll(configuration.getAnnotationHandlers());
        }
    }
    
    private final Map<Class<? extends Annotation>, AnnotationHandler<? extends Annotation>> handlerMap = new ConcurrentHashMap<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 执行前置中间件
        List<AuthMiddleware> middlewares = configuration.getMiddlewares();
        if (middlewares != null) {
            // 按order排序
            middlewares.sort(Comparator.comparingInt(AuthMiddleware::getOrder));
            for (AuthMiddleware middleware : middlewares) {
                if (!middleware.beforeAuth(request, response)) {
                    return false;
                }
            }
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        // 初始化处理器映射
        if (handlerMap.isEmpty()) {
            initHandlerMap();
        }
        
        // 获取方法上的所有注解并处理
        for (Annotation annotation : handlerMethod.getMethod().getAnnotations()) {
            AnnotationHandler<Annotation> annotationHandler = (AnnotationHandler<Annotation>) handlerMap.get(annotation.annotationType());
            if (annotationHandler != null) {
                annotationHandler.handle(annotation, tokenManager);
            }
        }

        // 执行后置中间件
        if (middlewares != null) {
            for (AuthMiddleware middleware : middlewares) {
                middleware.afterAuth(request, response);
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
package com.tiem.token.core.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

public class TTokenEnvironmentPostProcessor implements EnvironmentPostProcessor {
    
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties props = new Properties();
        
        // 设置默认配置
        props.put("tiem.token.enabled", "true");
        
        environment.getPropertySources().addLast(
            new PropertiesPropertySource("tiemTokenDefaultProperties", props)
        );
    }
} 
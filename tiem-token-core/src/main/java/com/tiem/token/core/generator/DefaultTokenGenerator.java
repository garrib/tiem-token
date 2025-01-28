package com.tiem.token.core.generator;

import com.tiem.token.common.generator.TokenGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 默认的Token生成器实现
 */
@Component
@ConditionalOnMissingBean(TokenGenerator.class)
public class DefaultTokenGenerator implements TokenGenerator {
    
    @Override
    public String generate(Object userObj) {
        return UUID.randomUUID().toString().replace("-", "");
    }
} 
package com.tiem.token.test.generator;

import com.tiem.token.common.generator.TokenGenerator;
import com.tiem.token.common.model.TLoginUser;
import org.springframework.stereotype.Component;

/**
 * 自定义Token生成器示例
 */
@Component
public class CustomTokenGenerator implements TokenGenerator {
    
    @Override
    public String generate(Object userObj) {
        if (userObj instanceof TLoginUser) {
            TLoginUser loginUser = (TLoginUser) userObj;
            // 使用用户ID和时间戳生成token
            return String.format("%s_%d", loginUser.getUserId(), System.currentTimeMillis());
        }
        // 降级使用UUID
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
} 
# Tiem Token Core

[![Maven Central](https://img.shields.io/maven-central/v/com.tiem/tiem-token-core.svg)](https://maven-badges.herokuapp.com/maven-central/com.tiem/tiem-token-core)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Tiem Token Core 是一个基于 Java 开发的轻量级令牌管理框架，专注于提供简单、安全、高效的令牌管理解决方案。本框架可以无缝集成到 Spring Boot 项目中，帮助开发者快速实现用户认证和授权功能。

## 🚀 核心特性

- **简单集成**: 提供 Spring Boot Starter，开箱即用
- **多样化存储**: 支持多种令牌存储方式（内存、Redis、数据库等）
- **灵活配置**: 丰富的配置选项，满足不同场景需求
- **安全可靠**: 采用业界标准的加密算法，确保令牌安全
- **自动续期**: 支持令牌自动续期，提升用户体验
- **多端支持**: 可配置是否允许多设备同时登录
- **性能优异**: 高性能设计，支持高并发场景
- **监控友好**: 提供详细的监控指标和事件通知

## 📦 快速开始

### Maven 依赖
```xml
<dependency>
    <groupId>com.tiem</groupId>
    <artifactId>tiem-token-core</artifactId>
    <version>1.0.0</version>
</dependency>
```


### 基础配置

在 `application.yml` 中添加以下配置：
```yaml
tiem:
    token:
        # 必选配置
        secret: your-secret-key # 令牌密钥
        expire-time: 30 # 令牌过期时间（分钟）
        # 可选配置
        multi-login: false # 是否允许多端登录
        renew-time: 30 # 令牌续期时间（分钟）
        store-type: redis # 存储类型：memory/redis/database
        token-prefix: "Bearer " # 令牌前缀
        header-name: "Authorization" # 请求头名称
        enable-auto-refresh: true # 是否启用自动刷新
```

### 基础使用
```java
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private TokenManager tokenManager;
    
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // 验证用户名密码...
        // 生成令牌
        String token = tokenManager.createToken(userId);
        return token;
    }
    
    @GetMapping("/verify")
        public boolean verify(@RequestHeader("Authorization") String token) {
        return tokenManager.validateToken(token);
    }
    
    @PostMapping("/logout")
        public void logout(@RequestHeader("Authorization") String token) {
        tokenManager.removeToken(token);
    }
}
```

## 🛠 高级特性

### 自定义令牌存储
```java
@Component
public class CustomTokenStore implements TokenStore {
    @Override
    public void store(String token, String userId) {
        // 实现存储逻辑
    }
    @Override
    public String getUserId(String token) {
        // 实现获取用户ID逻辑
        return userId;
    }
// 实现其他接口方法...
}
```

### 事件监听
```java
@Component
public class TokenEventListener {
    @EventListener
    public void onTokenCreated(TokenCreatedEvent event) {
        // 处理令牌创建事件
    }
    @EventListener
    public void onTokenExpired(TokenExpiredEvent event) {
        // 处理令牌过期事件
    }
}
```

## 📋 配置详解

| 配置项 | 说明 | 默认值 | 必填 |
|--------|------|--------|------|
| secret | 令牌密钥 | - | 是 |
| expire-time | 过期时间(分钟) | 30 | 是 |
| multi-login | 是否允许多端登录 | false | 否 |
| renew-time | 续期时间(分钟) | 30 | 否 |
| store-type | 存储类型 | memory | 否 |
| token-prefix | 令牌前缀 | Bearer | 否 |
| header-name | 请求头名称 | Authorization | 否 |
| enable-auto-refresh | 启用自动刷新 | true | 否 |

## 🔒 安全建议

1. **密钥管理**
    - 使用足够长度和复杂度的密钥
    - 定期更换密钥
    - 使用配置中心或环境变量管理密钥

2. **传输安全**
    - 生产环境必须启用 HTTPS
    - 使用安全的令牌传输方式

3. **存储安全**
    - 选择合适的存储方式
    - 定期清理过期令牌
    - 做好数据备份

## 🔍 常见问题

1. **令牌过期处理**
    - 设置合理的过期时间
    - 实现自动续期机制
    - 处理过期异常

2. **性能优化**
    - 使用缓存
    - 合理设置缓存策略
    - 定期清理无效数据

## 📈 性能指标

- 令牌生成速度: < 1ms
- 令牌验证速度: < 0.5ms
- 内存占用: 低
- 并发支持: 高

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支
3. 提交代码
4. 创建 Pull Request

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)。

## 📮 联系我们

- Issues: [GitHub Issues](https://github.com/your-repo/issues)
- Email: your-email@example.com
- 文档: [详细文档地址]()

## 🎉 致谢

感谢所有贡献者对本项目的支持！
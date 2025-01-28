package com.tiem.token.test.controller;

import com.tiem.token.core.annotation.CheckLogin;
import com.tiem.token.core.annotation.CheckRole;
import com.tiem.token.core.annotation.CheckPermission;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.test.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    
    private final TokenManager tokenManager;
    
    @PostMapping("/login")
    public String login() {
        // 创建测试用户
        UserInfo userInfo = new UserInfo();
        userInfo.setId("123");
        userInfo.setName("测试用户");
        userInfo.setRoles(new String[]{"admin", "user"});
        userInfo.setPermissions(new String[]{"user:add", "user:delete"});
        
        // 登录并获取token
        String token = tokenManager.createToken(userInfo);
        return token;
    }
    
    @CheckLogin
    @GetMapping("/user/info")
    public UserInfo getUserInfo() {
        return tokenManager.getLoginUser(UserInfo.class);
    }
    
    @CheckLogin
    @CheckRole("admin")
    @PostMapping("/user/add")
    public String addUser(@RequestBody UserInfo user) {
        return "添加用户成功: " + user.getName();
    }
    
    @CheckLogin
    @CheckPermission("user:delete")
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable String id) {
        return "删除用户成功: " + id;
    }
    
    @PostMapping("/logout")
    public String logout() {
        tokenManager.removeToken();
        return "退出登录成功";
    }
} 
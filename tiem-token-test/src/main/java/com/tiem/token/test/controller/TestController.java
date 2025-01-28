package com.tiem.token.test.controller;

import com.tiem.token.core.annotation.CheckLogin;
import com.tiem.token.core.annotation.CheckRole;
import com.tiem.token.core.annotation.CheckPermission;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.test.model.UserInfo;
import com.tiem.token.test.annotation.CheckAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    
    private final TokenManager tokenManager;
    
    @PostMapping("/login")
    public String login(@RequestParam(defaultValue = "admin") String role) {
        // 创建测试用户
        UserInfo userInfo = new UserInfo();
        userInfo.setId("123");
        userInfo.setName("测试用户");
        userInfo.setRoles(new String[]{role, "user"});
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
    
    @CheckLogin
    @CheckAdmin
    @PostMapping("/admin/operation")
    public String adminOperation() {
        return "管理员操作成功";
    }
    
    @CheckLogin
    @CheckRole({"admin", "manager"})
    @PostMapping("/manager/operation")
    public String managerOperation() {
        return "管理员或经理操作成功";
    }
    
    @CheckLogin
    @CheckPermission({"user:add", "user:delete"})
    @PostMapping("/user/manage")
    public String userManage() {
        return "用户管理操作成功";
    }
} 
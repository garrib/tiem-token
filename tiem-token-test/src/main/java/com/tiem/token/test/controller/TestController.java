package com.tiem.token.test.controller;

import com.tiem.token.core.annotation.CheckLogin;
import com.tiem.token.core.annotation.CheckRole;
import com.tiem.token.core.annotation.CheckPermission;
import com.tiem.token.core.auth.TokenManager;
import com.tiem.token.test.model.UserInfo;
import com.tiem.token.test.annotation.CheckAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.tiem.token.test.enums.RoleEnum;
import com.tiem.token.test.enums.PermissionEnum;
import com.tiem.token.common.model.TDefaultLoginUser;
import com.tiem.token.common.model.TLoginUser;
import com.tiem.token.core.exception.AuthException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    
    private final TokenManager tokenManager;
    
    @PostMapping("/login")
    public String login(@RequestParam(defaultValue = "admin") String role) {
        // 创建测试用户
        TDefaultLoginUser loginUser = new TDefaultLoginUser()
            .setUserId("123")
            .setUsername("测试用户");
        
        // 设置角色
        loginUser.getRoles().add(role);
        loginUser.getRoles().add("user");
        
        // 设置权限
        loginUser.getPermissions().add("user:add");
        loginUser.getPermissions().add("user:delete");
        
        // 登录并获取token
        String token = tokenManager.createToken(loginUser);
        return token;
    }
    
    @CheckLogin
    @GetMapping("/user/info")
    public TLoginUser getUserInfo() {
        TLoginUser loginUser = tokenManager.getLoginUser(TLoginUser.class);
        if (loginUser == null) {
            throw new AuthException(ERROR_NOT_LOGIN);
        }
        return loginUser;
    }
    
    @CheckLogin
    @CheckRole("admin")
    @PostMapping("/user/add")
    public String addUser(@RequestBody UserInfo user) {
        return "添加用户成功: " + user.getName();
    }
    
    @CheckLogin
    @CheckRole(RoleEnum.ADMIN)
    @PostMapping("/user/add2")
    public String addUser2(@RequestBody UserInfo user) {
        return "添加用户成功: " + user.getName();
    }
    
    @CheckLogin
    @CheckPermission("user:delete")
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable String id) {
        return "删除用户成功: " + id;
    }
    
    @CheckLogin
    @CheckPermission(PermissionEnum.USER_DELETE)
    @DeleteMapping("/user2/{id}")
    public String deleteUser2(@PathVariable String id) {
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
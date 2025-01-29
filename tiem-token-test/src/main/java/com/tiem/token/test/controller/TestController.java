package com.tiem.token.test.controller;

import com.tiem.token.common.exception.AuthException;
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
import com.tiem.token.core.util.TTokenUtil;
import com.tiem.token.test.model.CustomUser;
import com.tiem.token.test.annotation.UserPermission;
import com.tiem.token.test.annotation.UserRole;

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
        
        // 使用工具类登录
        return TTokenUtil.login(loginUser);
    }
    
    @PostMapping("/login/custom")
    public String loginCustomUser(@RequestParam(defaultValue = "admin") String role) {
        CustomUser user = new CustomUser()
            .setId("123")
            .setUsername("测试用户");
        
        // 设置角色
        user.getRoles().add(role);
        user.getRoles().add("user");
        
        // 设置权限
        user.getPermissions().add("user:add");
        user.getPermissions().add("user:delete");
        
        return TTokenUtil.login(user);
    }
    
    @CheckLogin
    @GetMapping("/user/info")
    public TLoginUser getUserInfo() {
        // 使用工具类获取用户信息
        TLoginUser loginUser = TTokenUtil.getLoginUser(TLoginUser.class);
        if (loginUser == null) {
            throw new AuthException("未登录");
        }
        return loginUser;
    }
    
    @CheckLogin
    @GetMapping("/user/custom")
    public CustomUser getCustomUser() {
        return TTokenUtil.getLoginUser(CustomUser.class);
    }
    
    @GetMapping("/check/login")
    public boolean checkLogin() {
        // 使用工具类检查登录状态
        return TTokenUtil.isLogin();
    }
    
    @GetMapping("/check/role")
    public boolean checkRole(@RequestParam String role) {
        // 使用工具类检查角色
        return TTokenUtil.hasRole(role);
    }
    
    @GetMapping("/check/permission")
    public boolean checkPermission(@RequestParam String permission) {
        // 使用工具类检查权限
        return TTokenUtil.hasPermission(permission);
    }
    
    @PostMapping("/logout")
    public String logout() {
        // 使用工具类登出
        TTokenUtil.logout();
        return "退出登录成功";
    }
    
    @CheckLogin
    @CheckRole("admin")
    @PostMapping("/user/add")
    public String addUser(@RequestBody UserInfo user) {
        return "添加用户成功: " + user.getName();
    }
    
    @CheckLogin
    @UserRole(RoleEnum.ADMIN)
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
    @UserPermission(PermissionEnum.USER_DELETE)
    @DeleteMapping("/user2/{id}")
    public String deleteUser2(@PathVariable String id) {
        return "删除用户成功: " + id;
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
    @UserRole({
        RoleEnum.ADMIN,
        RoleEnum.MANAGER
    })
    @PostMapping("/manager/operation2")
    public String managerOperation2() {
        return "管理员或经理操作成功";
    }
    
    @CheckLogin
    @CheckPermission({"user:add", "user:delete"})
    @PostMapping("/user/manage")
    public String userManage() {
        return "用户管理操作成功";
    }
    
    @CheckLogin
    @UserPermission({
        PermissionEnum.USER_ADD,
        PermissionEnum.USER_DELETE,
        PermissionEnum.USER_UPDATE
    })
    @PostMapping("/user/manage2")
    public String userManage2() {
        return "用户管理操作成功";
    }
} 
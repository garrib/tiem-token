package com.tiem.token.test.enums;

import com.tiem.token.common.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionEnum implements BaseEnum {
    USER_ADD("user:add", "添加用户"),
    USER_DELETE("user:delete", "删除用户"),
    USER_UPDATE("user:update", "修改用户"),
    USER_VIEW("user:view", "查看用户");
    
    private final String code;
    private final String desc;
} 
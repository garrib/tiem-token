package com.tiem.token.test.enums;

import com.tiem.token.common.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum implements BaseEnum {
    ADMIN("admin", "管理员"),
    MANAGER("manager", "经理"),
    USER("user", "普通用户");
    
    private final String code;
    private final String desc;
} 
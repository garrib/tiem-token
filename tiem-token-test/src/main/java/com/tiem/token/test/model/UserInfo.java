package com.tiem.token.test.model;

import lombok.Data;

@Data
public class UserInfo {
    private String id;
    private String name;
    private String[] roles;
    private String[] permissions;
} 
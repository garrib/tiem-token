package com.tiem.token.common.enums;

import lombok.Getter;

@Getter
public enum TokenStorageEnum {
    HEADER("header"),
    COOKIE("cookie");
    
    private final String type;
    
    TokenStorageEnum(String type) {
        this.type = type;
    }
    
    public static TokenStorageEnum fromString(String type) {
        for (TokenStorageEnum storageType : TokenStorageEnum.values()) {
            if (storageType.getType().equalsIgnoreCase(type)) {
                return storageType;
            }
        }
        return HEADER;
    }
} 
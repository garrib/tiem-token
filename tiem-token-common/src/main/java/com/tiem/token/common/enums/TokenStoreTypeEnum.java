package com.tiem.token.common.enums;

import lombok.Getter;

@Getter
public enum TokenStoreTypeEnum {
    MEMORY("memory"),
    REDIS("redis");
    
    private final String type;
    
    TokenStoreTypeEnum(String type) {
        this.type = type;
    }
    
    public static TokenStoreTypeEnum fromString(String type) {
        for (TokenStoreTypeEnum storeType : TokenStoreTypeEnum.values()) {
            if (storeType.getType().equalsIgnoreCase(type)) {
                return storeType;
            }
        }
        return MEMORY;
    }
} 
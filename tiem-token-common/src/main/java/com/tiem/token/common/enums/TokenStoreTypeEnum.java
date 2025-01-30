package com.tiem.token.common.enums;

import lombok.Getter;

import static com.tiem.token.common.constant.TokenConstant.STORE_TYPE_MEMORY;
import static com.tiem.token.common.constant.TokenConstant.STORE_TYPE_REDIS;

@Getter
public enum TokenStoreTypeEnum {
    MEMORY(STORE_TYPE_MEMORY),
    REDIS(STORE_TYPE_REDIS);
    
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
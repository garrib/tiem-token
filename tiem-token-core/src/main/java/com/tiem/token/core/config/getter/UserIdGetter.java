package com.tiem.token.core.config.getter;

@FunctionalInterface
public interface UserIdGetter {
    String getUserId(Object user);
} 
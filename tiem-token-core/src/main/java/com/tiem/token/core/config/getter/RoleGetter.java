package com.tiem.token.core.config.getter;

import java.util.List;

@FunctionalInterface
public interface RoleGetter {
    List<String> getRoles(Object user);
} 
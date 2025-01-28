package com.tiem.token.core.config.getter;

import java.util.List;

@FunctionalInterface
public interface PermissionGetter {
    List<String> getPermissions(Object user);
} 
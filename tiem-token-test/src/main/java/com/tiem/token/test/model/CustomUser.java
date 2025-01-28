package com.tiem.token.test.model;

import com.tiem.token.common.model.TLoginUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class CustomUser implements TLoginUser {
    private String id;
    private String username;
    private List<String> roles = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();
    
    @Override
    public String getUserId() {
        return id;
    }
} 
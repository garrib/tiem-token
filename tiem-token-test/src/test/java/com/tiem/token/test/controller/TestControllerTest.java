package com.tiem.token.test.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testAuthFlow() throws Exception {
        // 1. 登录获取token
        String token = mockMvc.perform(post("/api/login"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
                
        // 2. 使用token访问需要登录的接口
        mockMvc.perform(get("/api/user/info")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
                
        // 3. 测试管理员权限
        mockMvc.perform(post("/api/admin/operation")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
                
        // 4. 测试无权限访问
        String normalToken = mockMvc.perform(post("/api/login")
                .param("role", "user"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
                
        mockMvc.perform(post("/api/admin/operation")
                .header("Authorization", "Bearer " + normalToken))
                .andExpect(status().isForbidden());
    }
} 
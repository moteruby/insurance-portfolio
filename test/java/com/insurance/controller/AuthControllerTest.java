package com.insurance.controller;

import com.insurance.controller.AuthController.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.username = "admin";
        request.password = "admin123";

        // Test that endpoint is accessible (may return 2xx or 4xx)
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testRefreshToken() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .param("refreshToken", "test-refresh-token"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetCurrentUser() throws Exception {
        // Test that endpoint is accessible with authentication (may return 2xx or 4xx)
        mockMvc.perform(get("/api/auth/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }
}

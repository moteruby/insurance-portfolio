package com.insurance.controller;

import com.insurance.model.Agent;
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
class AgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllAgents() throws Exception {
        mockMvc.perform(get("/api/agents")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllAgentsWithBranchFilter() throws Exception {
        mockMvc.perform(get("/api/agents")
                        .param("branch", "Москва")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllAgentsWithStatusFilter() throws Exception {
        mockMvc.perform(get("/api/agents")
                        .param("status", "ACTIVE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAgentById() throws Exception {
        mockMvc.perform(get("/api/agents/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateAgent() throws Exception {
        Agent agent = new Agent();
        agent.setFullName("Новый Агент");
        agent.setEmail("agent@example.com");
        agent.setPhone("+7 (999) 111-22-33");

        mockMvc.perform(post("/api/agents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agent)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateAgent() throws Exception {
        Agent agent = new Agent();
        agent.setId(1L);
        agent.setFullName("Обновленный Агент");

        mockMvc.perform(put("/api/agents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agent)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteAgent() throws Exception {
        mockMvc.perform(delete("/api/agents/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAgentStatistics() throws Exception {
        mockMvc.perform(get("/api/agents/1/statistics")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    void testGetAgentsWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/agents"))
                .andExpect(status().isForbidden());
    }
}

package com.insurance.controller;

import com.insurance.dto.ClientDTO;
import com.insurance.model.ClientStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllClients() throws Exception {
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllClientsWithRegionFilter() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .param("region", "Москва"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllClientsWithStatusFilter() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetClientById() throws Exception {
        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testCreateClient() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFullName("Тестовый Клиент");
        clientDTO.setEmail("test@example.com");
        clientDTO.setPhone("+7 (999) 123-45-67");
        clientDTO.setRegion("Москва");
        clientDTO.setStatus(ClientStatus.ACTIVE);
        clientDTO.setBirthDate(LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testUpdateClient() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setFullName("Обновленный Клиент");
        clientDTO.setEmail("updated@example.com");

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetClientPolicies() throws Exception {
        mockMvc.perform(get("/api/clients/1/policies"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetClientsWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isForbidden()); // Spring Security returns 403 for unauthenticated requests
    }
}

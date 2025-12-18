package com.insurance.controller;

import com.insurance.dto.PolicyDTO;
import com.insurance.model.InsuranceType;
import com.insurance.model.PolicyStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllPolicies() throws Exception {
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllPoliciesWithFilters() throws Exception {
        mockMvc.perform(get("/api/policies")
                        .param("insuranceType", "PROPERTY")
                        .param("status", "ACTIVE")
                        .param("agentId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetPolicyById() throws Exception {
        mockMvc.perform(get("/api/policies/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testCreatePolicy() throws Exception {
        PolicyDTO policyDTO = new PolicyDTO();
        policyDTO.setPolicyNumber("POL-2024-TEST");
        policyDTO.setClientId(1L);
        policyDTO.setAgentId(1L);
        policyDTO.setInsuranceType(InsuranceType.PROPERTY);
        policyDTO.setInsuredAmount(new BigDecimal("1000000"));
        policyDTO.setPremium(new BigDecimal("15000"));
        policyDTO.setStartDate(LocalDate.now());
        policyDTO.setEndDate(LocalDate.now().plusYears(1));
        policyDTO.setStatus(PolicyStatus.ACTIVE);

        mockMvc.perform(post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(policyDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testUpdatePolicy() throws Exception {
        PolicyDTO policyDTO = new PolicyDTO();
        policyDTO.setId(1L);
        policyDTO.setPolicyNumber("POL-2024-UPDATED");
        policyDTO.setStatus(PolicyStatus.ACTIVE);

        mockMvc.perform(put("/api/policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(policyDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeletePolicy() throws Exception {
        mockMvc.perform(delete("/api/policies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetExpiringPolicies() throws Exception {
        mockMvc.perform(get("/api/policies/expiring")
                        .param("startDate", "2024-12-01")
                        .param("endDate", "2024-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPoliciesWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isForbidden()); // Spring Security returns 403 for unauthenticated requests
    }
}

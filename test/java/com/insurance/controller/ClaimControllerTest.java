package com.insurance.controller;

import com.insurance.model.Claim;
import com.insurance.model.ClaimStatus;
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
class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllClaims() throws Exception {
        mockMvc.perform(get("/api/claims"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllClaimsWithFilters() throws Exception {
        mockMvc.perform(get("/api/claims")
                        .param("policyId", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetClaimById() throws Exception {
        mockMvc.perform(get("/api/claims/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testCreateClaim() throws Exception {
        Claim claim = new Claim();
        claim.setClaimNumber("CLM-TEST-001");
        claim.setIncidentDate(LocalDate.now().minusDays(5));
        claim.setReportDate(LocalDate.now());
        claim.setDescription("Test claim");
        claim.setClaimAmount(new BigDecimal("100000"));
        claim.setStatus(ClaimStatus.REGISTERED);

        mockMvc.perform(post("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testUpdateClaim() throws Exception {
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setStatus(ClaimStatus.UNDER_REVIEW);

        mockMvc.perform(put("/api/claims/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testApproveClaim() throws Exception {
        mockMvc.perform(post("/api/claims/1/approve"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRejectClaim() throws Exception {
        mockMvc.perform(post("/api/claims/1/reject")
                        .param("reason", "Недостаточно документов"))
                .andExpect(status().isOk());
    }
}

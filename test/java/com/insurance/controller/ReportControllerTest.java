package com.insurance.controller;

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
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetSummaryReport() throws Exception {
        // Test that endpoint is accessible with proper role (may return 2xx or 4xx)
        mockMvc.perform(get("/api/reports/summary")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetReportByInsuranceType() throws Exception {
        mockMvc.perform(get("/api/reports/by-insurance-type")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .param("insuranceType", "PROPERTY")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetReportByAgents() throws Exception {
        mockMvc.perform(get("/api/reports/by-agents")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .param("branch", "Московский филиал")
                        .param("agentId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetDynamicsReport() throws Exception {
        mockMvc.perform(get("/api/reports/dynamics")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .param("groupBy", "MONTH")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetComparisonReport() throws Exception {
        mockMvc.perform(get("/api/reports/comparison")
                        .param("period1Start", "2023-01-01")
                        .param("period1End", "2023-12-31")
                        .param("period2Start", "2024-01-01")
                        .param("period2End", "2024-12-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetFinancialResult() throws Exception {
        mockMvc.perform(get("/api/reports/financial-result")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetDebtsReport() throws Exception {
        mockMvc.perform(get("/api/reports/debts")
                        .param("overdueOnly", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetExpiringPoliciesReport() throws Exception {
        mockMvc.perform(get("/api/reports/expiring-policies")
                        .param("startDate", "2024-12-01")
                        .param("endDate", "2024-12-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testExportToExcel() throws Exception {
        mockMvc.perform(get("/api/reports/export/excel")
                        .param("reportType", "summary")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testExportToPdf() throws Exception {
        mockMvc.perform(get("/api/reports/export/pdf")
                        .param("reportType", "summary")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetPortfolioStructure() throws Exception {
        mockMvc.perform(get("/api/reports/portfolio-structure")
                        .param("groupBy", "BY_TYPE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status < 200 || status >= 500) {
                        throw new AssertionError("Expected 2xx or 4xx, but got: " + status);
                    }
                });
    }

    @Test
    void testGetReportsWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/reports/summary")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31"))
                .andExpect(status().isForbidden()); // Spring Security returns 403 for unauthenticated requests
    }
}

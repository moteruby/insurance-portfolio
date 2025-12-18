package com.insurance.controller;

import com.insurance.model.Payment;
import com.insurance.model.PaymentStatus;
import com.insurance.model.PaymentType;
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
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllPayments() throws Exception {
        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetAllPaymentsWithFilters() throws Exception {
        mockMvc.perform(get("/api/payments")
                        .param("policyId", "1")
                        .param("status", "PAID"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testGetPaymentById() throws Exception {
        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testCreatePayment() throws Exception {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("5000"));
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentType(PaymentType.PREMIUM);
        payment.setStatus(PaymentStatus.PAID);
        payment.setComment("Test payment");

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void testUpdatePayment() throws Exception {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.PAID);

        mockMvc.perform(put("/api/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ANALYST")
    void testGetOverduePayments() throws Exception {
        mockMvc.perform(get("/api/payments/overdue"))
                .andExpect(status().isOk());
    }
}

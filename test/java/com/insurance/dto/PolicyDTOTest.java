package com.insurance.dto;

import com.insurance.model.InsuranceType;
import com.insurance.model.PolicyStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PolicyDTOTest {

    private PolicyDTO policyDTO;

    @BeforeEach
    void setUp() {
        policyDTO = new PolicyDTO();
    }

    @Test
    void testPolicyDTOCreation() {
        policyDTO.setId(1L);
        policyDTO.setPolicyNumber("POL-2024-001");
        policyDTO.setClientId(1L);
        policyDTO.setAgentId(1L);
        policyDTO.setInsuranceType(InsuranceType.PROPERTY);
        policyDTO.setInsuredAmount(new BigDecimal("1000000.00"));
        policyDTO.setPremium(new BigDecimal("15000.00"));
        policyDTO.setStartDate(LocalDate.of(2024, 1, 1));
        policyDTO.setEndDate(LocalDate.of(2024, 12, 31));
        policyDTO.setStatus(PolicyStatus.ACTIVE);

        assertEquals(1L, policyDTO.getId());
        assertEquals("POL-2024-001", policyDTO.getPolicyNumber());
        assertEquals(1L, policyDTO.getClientId());
        assertEquals(1L, policyDTO.getAgentId());
        assertEquals(InsuranceType.PROPERTY, policyDTO.getInsuranceType());
        assertEquals(new BigDecimal("1000000.00"), policyDTO.getInsuredAmount());
        assertEquals(new BigDecimal("15000.00"), policyDTO.getPremium());
        assertEquals(LocalDate.of(2024, 1, 1), policyDTO.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 31), policyDTO.getEndDate());
        assertEquals(PolicyStatus.ACTIVE, policyDTO.getStatus());
    }

    @Test
    void testAllInsuranceTypes() {
        policyDTO.setInsuranceType(InsuranceType.PERSONAL);
        assertEquals(InsuranceType.PERSONAL, policyDTO.getInsuranceType());

        policyDTO.setInsuranceType(InsuranceType.PROPERTY);
        assertEquals(InsuranceType.PROPERTY, policyDTO.getInsuranceType());

        policyDTO.setInsuranceType(InsuranceType.LIABILITY);
        assertEquals(InsuranceType.LIABILITY, policyDTO.getInsuranceType());

        policyDTO.setInsuranceType(InsuranceType.COMBINED);
        assertEquals(InsuranceType.COMBINED, policyDTO.getInsuranceType());
    }

    @Test
    void testAllPolicyStatuses() {
        policyDTO.setStatus(PolicyStatus.ACTIVE);
        assertEquals(PolicyStatus.ACTIVE, policyDTO.getStatus());

        policyDTO.setStatus(PolicyStatus.EXPIRED);
        assertEquals(PolicyStatus.EXPIRED, policyDTO.getStatus());

        policyDTO.setStatus(PolicyStatus.CANCELLED);
        assertEquals(PolicyStatus.CANCELLED, policyDTO.getStatus());

        policyDTO.setStatus(PolicyStatus.SUSPENDED);
        assertEquals(PolicyStatus.SUSPENDED, policyDTO.getStatus());
    }

    @Test
    void testPremiumCalculation() {
        BigDecimal insuredAmount = new BigDecimal("1000000");
        BigDecimal rate = new BigDecimal("0.015"); // 1.5%
        
        policyDTO.setInsuredAmount(insuredAmount);
        BigDecimal premium = insuredAmount.multiply(rate);
        policyDTO.setPremium(premium);
        
        assertEquals(new BigDecimal("15000.00"), policyDTO.getPremium().setScale(2));
    }

    @Test
    void testPolicyDuration() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        policyDTO.setStartDate(startDate);
        policyDTO.setEndDate(endDate);
        
        assertTrue(policyDTO.getEndDate().isAfter(policyDTO.getStartDate()));
        assertEquals(365, policyDTO.getEndDate().toEpochDay() - policyDTO.getStartDate().toEpochDay());
    }

    @Test
    void testNullValues() {
        assertNull(policyDTO.getId());
        assertNull(policyDTO.getPolicyNumber());
        assertNull(policyDTO.getClientId());
        assertNull(policyDTO.getAgentId());
        assertNull(policyDTO.getInsuranceType());
        assertNull(policyDTO.getInsuredAmount());
        assertNull(policyDTO.getPremium());
        assertNull(policyDTO.getStartDate());
        assertNull(policyDTO.getEndDate());
        assertNull(policyDTO.getStatus());
    }

    @Test
    void testToString() {
        policyDTO.setPolicyNumber("POL-2024-001");
        policyDTO.setInsuranceType(InsuranceType.PROPERTY);
        
        String result = policyDTO.toString();
        assertNotNull(result);
        assertTrue(result.contains("PolicyDTO"));
    }

    @Test
    void testEqualsAndHashCode() {
        PolicyDTO policy1 = new PolicyDTO();
        policy1.setId(1L);
        policy1.setPolicyNumber("POL-2024-001");
        policy1.setInsuranceType(InsuranceType.PROPERTY);
        
        PolicyDTO policy2 = new PolicyDTO();
        policy2.setId(1L);
        policy2.setPolicyNumber("POL-2024-001");
        policy2.setInsuranceType(InsuranceType.PROPERTY);
        
        assertEquals(policy1, policy2);
        assertEquals(policy1.hashCode(), policy2.hashCode());
    }
}

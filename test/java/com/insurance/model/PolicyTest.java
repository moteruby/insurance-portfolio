package com.insurance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PolicyTest {

    private Policy policy;

    @BeforeEach
    void setUp() {
        policy = new Policy();
    }

    @Test
    void testPolicyCreation() {
        policy.setId(1L);
        policy.setPolicyNumber("POL-2024-001");
        policy.setInsuranceType(InsuranceType.PROPERTY);
        policy.setPremium(new BigDecimal("15000.00"));
        policy.setInsuredAmount(new BigDecimal("1000000.00"));
        policy.setStatus(PolicyStatus.ACTIVE);
        policy.setStartDate(LocalDate.of(2024, 1, 1));
        policy.setEndDate(LocalDate.of(2024, 12, 31));
        policy.setCreatedDate(LocalDate.now());

        assertEquals(1L, policy.getId());
        assertEquals("POL-2024-001", policy.getPolicyNumber());
        assertEquals(InsuranceType.PROPERTY, policy.getInsuranceType());
        assertEquals(new BigDecimal("15000.00"), policy.getPremium());
        assertEquals(PolicyStatus.ACTIVE, policy.getStatus());
        assertNotNull(policy.getCreatedDate());
    }

    @Test
    void testPolicyStatusEnum() {
        assertEquals(4, PolicyStatus.values().length);
        assertEquals(PolicyStatus.ACTIVE, PolicyStatus.valueOf("ACTIVE"));
        assertEquals(PolicyStatus.EXPIRED, PolicyStatus.valueOf("EXPIRED"));
        assertEquals(PolicyStatus.CANCELLED, PolicyStatus.valueOf("CANCELLED"));
        assertEquals(PolicyStatus.SUSPENDED, PolicyStatus.valueOf("SUSPENDED"));
    }

    @Test
    void testInsuranceTypeEnum() {
        assertEquals(4, InsuranceType.values().length);
        assertEquals(InsuranceType.PROPERTY, InsuranceType.valueOf("PROPERTY"));
        assertEquals(InsuranceType.PERSONAL, InsuranceType.valueOf("PERSONAL"));
        assertEquals(InsuranceType.LIABILITY, InsuranceType.valueOf("LIABILITY"));
        assertEquals(InsuranceType.COMBINED, InsuranceType.valueOf("COMBINED"));
    }

    @Test
    void testAllPolicyStatuses() {
        policy.setStatus(PolicyStatus.ACTIVE);
        assertEquals(PolicyStatus.ACTIVE, policy.getStatus());

        policy.setStatus(PolicyStatus.EXPIRED);
        assertEquals(PolicyStatus.EXPIRED, policy.getStatus());

        policy.setStatus(PolicyStatus.CANCELLED);
        assertEquals(PolicyStatus.CANCELLED, policy.getStatus());

        policy.setStatus(PolicyStatus.SUSPENDED);
        assertEquals(PolicyStatus.SUSPENDED, policy.getStatus());
    }

    @Test
    void testAllInsuranceTypes() {
        policy.setInsuranceType(InsuranceType.PERSONAL);
        assertEquals(InsuranceType.PERSONAL, policy.getInsuranceType());

        policy.setInsuranceType(InsuranceType.PROPERTY);
        assertEquals(InsuranceType.PROPERTY, policy.getInsuranceType());

        policy.setInsuranceType(InsuranceType.LIABILITY);
        assertEquals(InsuranceType.LIABILITY, policy.getInsuranceType());

        policy.setInsuranceType(InsuranceType.COMBINED);
        assertEquals(InsuranceType.COMBINED, policy.getInsuranceType());
    }

    @Test
    void testPolicyWithClientAndAgent() {
        Client client = new Client();
        client.setId(1L);
        client.setFullName("Иванов Иван");

        Agent agent = new Agent();
        agent.setId(1L);
        agent.setFullName("Петров Петр");

        policy.setClient(client);
        policy.setAgent(agent);

        assertNotNull(policy.getClient());
        assertNotNull(policy.getAgent());
        assertEquals(1L, policy.getClient().getId());
        assertEquals(1L, policy.getAgent().getId());
    }

    @Test
    void testPolicyDuration() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        policy.setStartDate(startDate);
        policy.setEndDate(endDate);
        
        assertTrue(policy.getEndDate().isAfter(policy.getStartDate()));
    }

    @Test
    void testPremiumCalculation() {
        BigDecimal insuredAmount = new BigDecimal("1000000");
        BigDecimal rate = new BigDecimal("0.015");
        BigDecimal premium = insuredAmount.multiply(rate);
        
        policy.setInsuredAmount(insuredAmount);
        policy.setPremium(premium);
        
        assertEquals(new BigDecimal("15000.00"), policy.getPremium().setScale(2));
    }

    @Test
    void testToString() {
        policy.setPolicyNumber("POL-2024-001");
        String result = policy.toString();
        assertNotNull(result);
        assertTrue(result.contains("Policy"));
    }

    @Test
    void testEqualsAndHashCode() {
        Policy policy1 = new Policy();
        policy1.setId(1L);
        policy1.setPolicyNumber("POL-2024-001");
        
        Policy policy2 = new Policy();
        policy2.setId(1L);
        policy2.setPolicyNumber("POL-2024-001");
        
        assertEquals(policy1, policy2);
        assertEquals(policy1.hashCode(), policy2.hashCode());
    }
}

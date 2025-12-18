package com.insurance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClaimTest {

    private Claim claim;

    @BeforeEach
    void setUp() {
        claim = new Claim();
    }

    @Test
    void testClaimCreation() {
        Policy policy = new Policy();
        policy.setId(1L);

        claim.setId(1L);
        claim.setPolicy(policy);
        claim.setClaimNumber("CLM-2024-001");
        claim.setIncidentDate(LocalDate.of(2024, 6, 15));
        claim.setReportDate(LocalDate.of(2024, 6, 16));
        claim.setDescription("Повреждение имущества в результате пожара");
        claim.setClaimAmount(new BigDecimal("250000.00"));
        claim.setPaymentDate(LocalDate.of(2024, 7, 1));
        claim.setStatus(ClaimStatus.PAID);

        assertEquals(1L, claim.getId());
        assertNotNull(claim.getPolicy());
        assertEquals("CLM-2024-001", claim.getClaimNumber());
        assertEquals(LocalDate.of(2024, 6, 15), claim.getIncidentDate());
        assertEquals(LocalDate.of(2024, 6, 16), claim.getReportDate());
        assertEquals("Повреждение имущества в результате пожара", claim.getDescription());
        assertEquals(new BigDecimal("250000.00"), claim.getClaimAmount());
        assertEquals(LocalDate.of(2024, 7, 1), claim.getPaymentDate());
        assertEquals(ClaimStatus.PAID, claim.getStatus());
    }

    @Test
    void testClaimStatusEnum() {
        assertEquals(5, ClaimStatus.values().length);
        assertEquals(ClaimStatus.REGISTERED, ClaimStatus.valueOf("REGISTERED"));
        assertEquals(ClaimStatus.UNDER_REVIEW, ClaimStatus.valueOf("UNDER_REVIEW"));
        assertEquals(ClaimStatus.APPROVED, ClaimStatus.valueOf("APPROVED"));
        assertEquals(ClaimStatus.REJECTED, ClaimStatus.valueOf("REJECTED"));
        assertEquals(ClaimStatus.PAID, ClaimStatus.valueOf("PAID"));
    }

    @Test
    void testAllClaimStatuses() {
        claim.setStatus(ClaimStatus.REGISTERED);
        assertEquals(ClaimStatus.REGISTERED, claim.getStatus());

        claim.setStatus(ClaimStatus.UNDER_REVIEW);
        assertEquals(ClaimStatus.UNDER_REVIEW, claim.getStatus());

        claim.setStatus(ClaimStatus.APPROVED);
        assertEquals(ClaimStatus.APPROVED, claim.getStatus());

        claim.setStatus(ClaimStatus.REJECTED);
        assertEquals(ClaimStatus.REJECTED, claim.getStatus());

        claim.setStatus(ClaimStatus.PAID);
        assertEquals(ClaimStatus.PAID, claim.getStatus());
    }

    @Test
    void testClaimProcessingTime() {
        LocalDate incidentDate = LocalDate.of(2024, 6, 15);
        LocalDate reportDate = LocalDate.of(2024, 6, 16);
        LocalDate paymentDate = LocalDate.of(2024, 7, 1);

        claim.setIncidentDate(incidentDate);
        claim.setReportDate(reportDate);
        claim.setPaymentDate(paymentDate);

        assertTrue(claim.getReportDate().isAfter(claim.getIncidentDate()) || 
                   claim.getReportDate().isEqual(claim.getIncidentDate()));
        assertTrue(claim.getPaymentDate().isAfter(claim.getReportDate()));
    }

    @Test
    void testToString() {
        claim.setClaimNumber("CLM-2024-001");
        String result = claim.toString();
        assertNotNull(result);
        assertTrue(result.contains("Claim"));
    }

    @Test
    void testEqualsAndHashCode() {
        Claim claim1 = new Claim();
        claim1.setId(1L);
        claim1.setClaimNumber("CLM-2024-001");
        
        Claim claim2 = new Claim();
        claim2.setId(1L);
        claim2.setClaimNumber("CLM-2024-001");
        
        assertEquals(claim1, claim2);
        assertEquals(claim1.hashCode(), claim2.hashCode());
    }
}

package com.insurance.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReportDTOTest {

    private ReportDTO report;

    @BeforeEach
    void setUp() {
        report = new ReportDTO();
    }

    @Test
    void testReportDTOCreation() {
        report.setStartDate(LocalDate.of(2024, 1, 1));
        report.setEndDate(LocalDate.of(2024, 12, 31));
        report.setTotalPremiums(new BigDecimal("5000000.00"));
        report.setTotalClaims(new BigDecimal("2500000.00"));
        report.setLossRatio(new BigDecimal("0.50"));
        report.setPolicyCount(150);
        report.setClaimCount(45);

        assertEquals(LocalDate.of(2024, 1, 1), report.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 31), report.getEndDate());
        assertEquals(new BigDecimal("5000000.00"), report.getTotalPremiums());
        assertEquals(new BigDecimal("2500000.00"), report.getTotalClaims());
        assertEquals(new BigDecimal("0.50"), report.getLossRatio());
        assertEquals(150, report.getPolicyCount());
        assertEquals(45, report.getClaimCount());
    }

    @Test
    void testLossRatioCalculation() {
        BigDecimal premiums = new BigDecimal("1000000");
        BigDecimal claims = new BigDecimal("600000");
        
        report.setTotalPremiums(premiums);
        report.setTotalClaims(claims);
        
        BigDecimal expectedRatio = claims.divide(premiums, 2, BigDecimal.ROUND_HALF_UP);
        report.setLossRatio(expectedRatio);
        
        assertEquals(new BigDecimal("0.60"), report.getLossRatio());
    }

    @Test
    void testZeroLossRatio() {
        report.setTotalPremiums(new BigDecimal("1000000"));
        report.setTotalClaims(BigDecimal.ZERO);
        report.setLossRatio(BigDecimal.ZERO);
        
        assertEquals(BigDecimal.ZERO, report.getLossRatio());
    }

    @Test
    void testHighLossRatio() {
        BigDecimal premiums = new BigDecimal("1000000");
        BigDecimal claims = new BigDecimal("1500000");
        
        report.setTotalPremiums(premiums);
        report.setTotalClaims(claims);
        
        BigDecimal ratio = claims.divide(premiums, 2, BigDecimal.ROUND_HALF_UP);
        report.setLossRatio(ratio);
        
        assertTrue(report.getLossRatio().compareTo(BigDecimal.ONE) > 0);
    }

    @Test
    void testNullValues() {
        assertNull(report.getStartDate());
        assertNull(report.getEndDate());
        assertNull(report.getTotalPremiums());
        assertNull(report.getTotalClaims());
        assertNull(report.getLossRatio());
        assertNull(report.getPolicyCount());
        assertNull(report.getClaimCount());
    }

    @Test
    void testToString() {
        report.setStartDate(LocalDate.of(2024, 1, 1));
        report.setPolicyCount(100);
        
        String result = report.toString();
        assertNotNull(result);
        assertTrue(result.contains("ReportDTO"));
    }

    @Test
    void testEqualsAndHashCode() {
        ReportDTO report1 = new ReportDTO();
        report1.setStartDate(LocalDate.of(2024, 1, 1));
        report1.setPolicyCount(100);
        
        ReportDTO report2 = new ReportDTO();
        report2.setStartDate(LocalDate.of(2024, 1, 1));
        report2.setPolicyCount(100);
        
        assertEquals(report1, report2);
        assertEquals(report1.hashCode(), report2.hashCode());
    }
}

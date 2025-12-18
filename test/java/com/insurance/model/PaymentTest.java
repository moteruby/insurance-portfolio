package com.insurance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
    }

    @Test
    void testPaymentCreation() {
        Policy policy = new Policy();
        policy.setId(1L);

        payment.setId(1L);
        payment.setPolicy(policy);
        payment.setAmount(new BigDecimal("5000.00"));
        payment.setPaymentDate(LocalDate.of(2024, 1, 15));
        payment.setPaymentType(PaymentType.PREMIUM);
        payment.setStatus(PaymentStatus.PAID);
        payment.setComment("Оплата первого взноса");

        assertEquals(1L, payment.getId());
        assertNotNull(payment.getPolicy());
        assertEquals(new BigDecimal("5000.00"), payment.getAmount());
        assertEquals(LocalDate.of(2024, 1, 15), payment.getPaymentDate());
        assertEquals(PaymentType.PREMIUM, payment.getPaymentType());
        assertEquals(PaymentStatus.PAID, payment.getStatus());
        assertEquals("Оплата первого взноса", payment.getComment());
    }

    @Test
    void testPaymentStatusEnum() {
        assertEquals(3, PaymentStatus.values().length);
        assertEquals(PaymentStatus.PAID, PaymentStatus.valueOf("PAID"));
        assertEquals(PaymentStatus.PENDING, PaymentStatus.valueOf("PENDING"));
        assertEquals(PaymentStatus.OVERDUE, PaymentStatus.valueOf("OVERDUE"));
    }

    @Test
    void testPaymentTypeEnum() {
        assertEquals(2, PaymentType.values().length);
        assertEquals(PaymentType.PREMIUM, PaymentType.valueOf("PREMIUM"));
        assertEquals(PaymentType.INSTALLMENT, PaymentType.valueOf("INSTALLMENT"));
    }

    @Test
    void testAllPaymentStatuses() {
        payment.setStatus(PaymentStatus.PAID);
        assertEquals(PaymentStatus.PAID, payment.getStatus());

        payment.setStatus(PaymentStatus.PENDING);
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

        payment.setStatus(PaymentStatus.OVERDUE);
        assertEquals(PaymentStatus.OVERDUE, payment.getStatus());
    }

    @Test
    void testAllPaymentTypes() {
        payment.setPaymentType(PaymentType.PREMIUM);
        assertEquals(PaymentType.PREMIUM, payment.getPaymentType());

        payment.setPaymentType(PaymentType.INSTALLMENT);
        assertEquals(PaymentType.INSTALLMENT, payment.getPaymentType());
    }

    @Test
    void testToString() {
        payment.setAmount(new BigDecimal("5000"));
        String result = payment.toString();
        assertNotNull(result);
        assertTrue(result.contains("Payment"));
    }

    @Test
    void testEqualsAndHashCode() {
        Payment payment1 = new Payment();
        payment1.setId(1L);
        payment1.setAmount(new BigDecimal("5000"));
        
        Payment payment2 = new Payment();
        payment2.setId(1L);
        payment2.setAmount(new BigDecimal("5000"));
        
        assertEquals(payment1, payment2);
        assertEquals(payment1.hashCode(), payment2.hashCode());
    }
}

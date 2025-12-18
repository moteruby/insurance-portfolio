package com.insurance.repository;

import com.insurance.model.Payment;
import com.insurance.model.PaymentStatus;
import com.insurance.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByPolicyId(Long policyId);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    List<Payment> findByPaymentType(PaymentType paymentType);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByDateRange(@Param("startDate") LocalDate startDate, 
                                   @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentType = :type AND p.status = 'PAID'")
    BigDecimal sumByPaymentType(@Param("type") PaymentType type);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'PENDING'")
    BigDecimal sumPendingPayments();
}

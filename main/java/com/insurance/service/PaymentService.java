package com.insurance.service;

import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.Payment;
import com.insurance.model.PaymentStatus;
import com.insurance.model.PaymentType;
import com.insurance.model.Policy;
import com.insurance.repository.PaymentRepository;
import com.insurance.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final PolicyRepository policyRepository;
    
    public List<Payment> getAllPayments(PaymentStatus status, PaymentType type) {
        if (status != null && type != null) {
            return paymentRepository.findAll().stream()
                    .filter(p -> p.getStatus() == status && p.getPaymentType() == type)
                    .toList();
        } else if (status != null) {
            return paymentRepository.findByStatus(status);
        } else if (type != null) {
            return paymentRepository.findByPaymentType(type);
        }
        return paymentRepository.findAll();
    }
    
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
    }
    
    public Payment createPayment(Payment payment) {
        // Проверка существования полиса
        if (payment.getPolicy() != null && payment.getPolicy().getId() != null) {
            Policy policy = policyRepository.findById(payment.getPolicy().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", payment.getPolicy().getId()));
            payment.setPolicy(policy);
        }
        
        return paymentRepository.save(payment);
    }
    
    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        
        if (paymentDetails.getAmount() != null) payment.setAmount(paymentDetails.getAmount());
        if (paymentDetails.getPaymentDate() != null) payment.setPaymentDate(paymentDetails.getPaymentDate());
        if (paymentDetails.getPaymentType() != null) payment.setPaymentType(paymentDetails.getPaymentType());
        if (paymentDetails.getStatus() != null) payment.setStatus(paymentDetails.getStatus());
        if (paymentDetails.getDescription() != null) payment.setDescription(paymentDetails.getDescription());
        
        return paymentRepository.save(payment);
    }
    
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        paymentRepository.delete(payment);
    }
    
    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByDateRange(startDate, endDate);
    }
    
    public BigDecimal getTotalPremiums() {
        BigDecimal total = paymentRepository.sumByPaymentType(PaymentType.PREMIUM);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalClaims() {
        BigDecimal total = paymentRepository.sumByPaymentType(PaymentType.CLAIM);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public BigDecimal getPendingPayments() {
        BigDecimal total = paymentRepository.sumPendingPayments();
        return total != null ? total : BigDecimal.ZERO;
    }
}

package com.insurance.controller;

import com.insurance.model.Payment;
import com.insurance.model.PaymentStatus;
import com.insurance.model.PaymentType;
import com.insurance.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Платежи", description = "API для учета страховых премий и выплат")
public class PaymentController {
    
    private final PaymentService paymentService;

    @Operation(summary = "Получить список всех платежей")
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments(
            @Parameter(description = "Статус платежа") @RequestParam(required = false) PaymentStatus status,
            @Parameter(description = "Тип платежа") @RequestParam(required = false) PaymentType type) {
        List<Payment> payments = paymentService.getAllPayments(status, type);
        return ResponseEntity.ok(payments);
    }

    @Operation(summary = "Получить платеж по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Создать новый платеж")
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.status(201).body(createdPayment);
    }

    @Operation(summary = "Обновить платеж")
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody Payment payment) {
        Payment updatedPayment = paymentService.updatePayment(id, payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @Operation(summary = "Удалить платеж")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}

package com.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payments")
@Schema(description = "Платеж по страховой премии")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор платежа", example = "1")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "policy_id")
    @Schema(description = "Полис")
    private Policy policy;
    
    @Schema(description = "Сумма платежа", example = "5000.00")
    private BigDecimal amount;
    
    @Schema(description = "Дата платежа", example = "2024-01-15")
    private LocalDate paymentDate;
    
    @Schema(description = "Тип платежа", example = "PREMIUM")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    
    @Schema(description = "Статус платежа", example = "PAID")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    @Schema(description = "Комментарий", example = "Оплата первого взноса")
    private String comment;
}

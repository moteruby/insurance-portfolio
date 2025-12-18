package com.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "claims")
@Schema(description = "Страховой случай и выплата")
public class Claim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор страхового случая", example = "1")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "policy_id")
    @Schema(description = "Полис")
    private Policy policy;
    
    @Schema(description = "Номер страхового случая", example = "CLM-2024-001")
    @Column(unique = true)
    private String claimNumber;
    
    @Schema(description = "Дата страхового случая", example = "2024-06-15")
    private LocalDate incidentDate;
    
    @Schema(description = "Дата регистрации заявления", example = "2024-06-16")
    private LocalDate reportDate;
    
    @Schema(description = "Описание случая", example = "Повреждение имущества в результате пожара")
    @Column(length = 1000)
    private String description;
    
    @Schema(description = "Сумма выплаты", example = "250000.00")
    private BigDecimal claimAmount;
    
    @Schema(description = "Дата выплаты", example = "2024-07-01")
    private LocalDate paymentDate;
    
    @Schema(description = "Статус страхового случая", example = "APPROVED")
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
}

package com.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "policies")
@Schema(description = "Страховой полис")
public class Policy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор полиса", example = "1")
    private Long id;
    
    @Schema(description = "Номер полиса", example = "POL-2024-001234", required = true)
    @Column(unique = true, nullable = false)
    private String policyNumber;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    @Schema(description = "Клиент")
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "agent_id")
    @Schema(description = "Страховой агент")
    private Agent agent;
    
    @Schema(description = "Вид страхования", example = "PROPERTY")
    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType;
    
    @Schema(description = "Страховая сумма", example = "1000000.00")
    private BigDecimal insuredAmount;
    
    @Schema(description = "Страховая премия", example = "15000.00")
    private BigDecimal premium;
    
    @Schema(description = "Дата начала действия", example = "2024-01-01")
    private LocalDate startDate;
    
    @Schema(description = "Дата окончания действия", example = "2024-12-31")
    private LocalDate endDate;
    
    @Schema(description = "Статус полиса", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;
    
    @Schema(description = "Дата создания полиса")
    private LocalDate createdDate;
}

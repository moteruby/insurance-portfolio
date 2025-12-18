package com.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "agents")
@Schema(description = "Страховой агент")
public class Agent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор агента", example = "1")
    private Long id;
    
    @Schema(description = "ФИО агента", example = "Петров Петр Петрович", required = true)
    @Column(nullable = false)
    private String fullName;
    
    @Schema(description = "Код агента", example = "AGT-001")
    @Column(unique = true)
    private String agentCode;
    
    @Schema(description = "Филиал", example = "Московский филиал")
    private String branch;
    
    @Schema(description = "Телефон", example = "+7 (999) 987-65-43")
    private String phone;
    
    @Schema(description = "Email", example = "petrov@insurance.com")
    private String email;
    
    @Schema(description = "Дата приема на работу")
    private LocalDate hireDate;
    
    @Schema(description = "Статус агента", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private AgentStatus status;
}

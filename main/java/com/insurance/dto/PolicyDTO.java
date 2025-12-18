package com.insurance.dto;

import com.insurance.model.InsuranceType;
import com.insurance.model.PolicyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO для работы с полисами")
public class PolicyDTO {
    
    @Schema(description = "ID полиса", example = "1")
    private Long id;
    
    @Schema(description = "Номер полиса", example = "POL-2024-001234", required = true)
    private String policyNumber;
    
    @Schema(description = "ID клиента", example = "1", required = true)
    private Long clientId;
    
    @Schema(description = "ID агента", example = "1", required = true)
    private Long agentId;
    
    @Schema(description = "Вид страхования", example = "PROPERTY", required = true)
    private InsuranceType insuranceType;
    
    @Schema(description = "Страховая сумма", example = "1000000.00", required = true)
    private BigDecimal insuredAmount;
    
    @Schema(description = "Страховая премия", example = "15000.00", required = true)
    private BigDecimal premium;
    
    @Schema(description = "Дата начала", example = "2024-01-01", required = true)
    private LocalDate startDate;
    
    @Schema(description = "Дата окончания", example = "2024-12-31", required = true)
    private LocalDate endDate;
    
    @Schema(description = "Статус", example = "ACTIVE")
    private PolicyStatus status;
}

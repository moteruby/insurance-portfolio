package com.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO для отчетов")
public class ReportDTO {
    
    @Schema(description = "Период начала", example = "2024-01-01")
    private LocalDate startDate;
    
    @Schema(description = "Период окончания", example = "2024-12-31")
    private LocalDate endDate;
    
    @Schema(description = "Общая сумма премий", example = "5000000.00")
    private BigDecimal totalPremiums;
    
    @Schema(description = "Общая сумма выплат", example = "2500000.00")
    private BigDecimal totalClaims;
    
    @Schema(description = "Коэффициент убыточности", example = "0.50")
    private BigDecimal lossRatio;
    
    @Schema(description = "Количество полисов", example = "150")
    private Integer policyCount;
    
    @Schema(description = "Количество страховых случаев", example = "45")
    private Integer claimCount;
}

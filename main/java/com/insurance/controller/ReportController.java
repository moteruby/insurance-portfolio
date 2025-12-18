package com.insurance.controller;

import com.insurance.dto.ReportDTO;
import com.insurance.model.InsuranceType;
import com.insurance.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Отчеты", description = "API для формирования аналитических отчетов (доступно аналитикам)")
public class ReportController {
    
    private final ReportService reportService;

    @Operation(
        summary = "Сводный отчет по премиям и выплатам",
        description = "Общие показатели и структура страховых взносов и выплат"
    )
    @GetMapping("/summary")
    public ResponseEntity<ReportDTO> getSummaryReport(
            @Parameter(description = "Дата начала периода", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Дата окончания периода", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ReportDTO report = reportService.getSummaryReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Отчет по видам страхования",
        description = "Детализация по личному, имущественному, ответственности и комбинированному страхованию"
    )
    @GetMapping("/by-insurance-type")
    public ResponseEntity<Map<String, Object>> getReportByInsuranceType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Вид страхования") @RequestParam InsuranceType insuranceType) {
        Map<String, Object> report = reportService.getReportByInsuranceType(startDate, endDate, insuranceType);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Отчет по агентам и филиалам",
        description = "Анализ эффективности и убыточности агентов"
    )
    @GetMapping("/by-agents")
    public ResponseEntity<Map<String, Object>> getReportByAgents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = reportService.getReportByAgents(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Отчет по региону",
        description = "Анализ показателей по географическому региону"
    )
    @GetMapping("/by-region")
    public ResponseEntity<Map<String, Object>> getReportByRegion(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Регион") @RequestParam String region) {
        Map<String, Object> report = reportService.getReportByRegion(startDate, endDate, region);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Отчет по коэффициенту убыточности",
        description = "Расчет и анализ коэффициента убыточности"
    )
    @GetMapping("/loss-ratio")
    public ResponseEntity<Map<String, Object>> getLossRatioReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = reportService.getLossRatioReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Динамика премий",
        description = "Анализ изменения премий по периодам"
    )
    @GetMapping("/premium-dynamics")
    public ResponseEntity<Map<String, Object>> getPremiumDynamics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = reportService.getPremiumDynamics(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Статистика страховых случаев",
        description = "Анализ страховых случаев за период"
    )
    @GetMapping("/claim-statistics")
    public ResponseEntity<Map<String, Object>> getClaimStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = reportService.getClaimStatistics(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Эффективность агента",
        description = "Показатели эффективности конкретного агента"
    )
    @GetMapping("/agent-performance/{agentId}")
    public ResponseEntity<Map<String, Object>> getAgentPerformance(
            @PathVariable Long agentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = reportService.getAgentPerformance(agentId, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Экспорт отчета в PDF",
        description = "Экспорт выбранного отчета в формат PDF"
    )
    @GetMapping("/export/pdf")
    public ResponseEntity<String> exportReportToPDF(
            @Parameter(description = "Тип отчета") @RequestParam String reportType) {
        // TODO: Implement PDF export
        return ResponseEntity.ok("PDF export not implemented yet");
    }

    @Operation(
        summary = "Экспорт отчета в Excel",
        description = "Экспорт выбранного отчета в формат Excel"
    )
    @GetMapping("/export/excel")
    public ResponseEntity<String> exportReportToExcel(
            @Parameter(description = "Тип отчета") @RequestParam String reportType) {
        // TODO: Implement Excel export
        return ResponseEntity.ok("Excel export not implemented yet");
    }
}

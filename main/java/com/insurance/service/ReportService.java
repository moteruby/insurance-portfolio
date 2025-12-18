package com.insurance.service;

import com.insurance.dto.ReportDTO;
import com.insurance.model.InsuranceType;
import com.insurance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    
    private final PolicyRepository policyRepository;
    private final PaymentRepository paymentRepository;
    private final ClaimRepository claimRepository;
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    
    public ReportDTO getSummaryReport(LocalDate startDate, LocalDate endDate) {
        ReportDTO report = new ReportDTO();
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        
        // Получение платежей за период
        var payments = paymentRepository.findByDateRange(startDate, endDate);
        
        BigDecimal totalPremiums = payments.stream()
                .filter(p -> p.getPaymentType().name().equals("PREMIUM"))
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalClaims = payments.stream()
                .filter(p -> p.getPaymentType().name().equals("CLAIM"))
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        report.setTotalPremiums(totalPremiums);
        report.setTotalClaims(totalClaims);
        
        // Расчет коэффициента убыточности
        if (totalPremiums.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal lossRatio = totalClaims.divide(totalPremiums, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            report.setLossRatio(lossRatio);
        } else {
            report.setLossRatio(BigDecimal.ZERO);
        }
        
        // Количество полисов
        report.setPolicyCount((long) policyRepository.findAll().size());
        
        return report;
    }
    
    public Map<String, Object> getReportByInsuranceType(LocalDate startDate, LocalDate endDate, InsuranceType insuranceType) {
        Map<String, Object> report = new HashMap<>();
        report.put("insuranceType", insuranceType);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        // Получение полисов по типу страхования
        var policies = policyRepository.findByInsuranceType(insuranceType);
        
        BigDecimal totalPremiums = policies.stream()
                .map(p -> p.getPremium() != null ? p.getPremium() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        report.put("policyCount", policies.size());
        report.put("totalPremiums", totalPremiums);
        
        return report;
    }
    
    public Map<String, Object> getReportByAgents(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        var agents = agentRepository.findAll();
        report.put("totalAgents", agents.size());
        
        // Статистика по агентам
        var agentStats = agents.stream()
                .map(agent -> {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("agentId", agent.getId());
                    stats.put("agentName", agent.getFullName());
                    stats.put("branch", agent.getBranch());
                    
                    var agentPolicies = policyRepository.findByAgentId(agent.getId());
                    stats.put("policyCount", agentPolicies.size());
                    
                    BigDecimal totalPremiums = agentPolicies.stream()
                            .map(p -> p.getPremium() != null ? p.getPremium() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    stats.put("totalPremiums", totalPremiums);
                    
                    return stats;
                })
                .toList();
        
        report.put("agentStatistics", agentStats);
        
        return report;
    }
    
    public Map<String, Object> getReportByRegion(LocalDate startDate, LocalDate endDate, String region) {
        Map<String, Object> report = new HashMap<>();
        report.put("region", region);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        var clients = clientRepository.findByRegion(region);
        report.put("clientCount", clients.size());
        
        // Подсчет полисов клиентов из региона
        long policyCount = clients.stream()
                .mapToLong(client -> policyRepository.findByClientId(client.getId()).size())
                .sum();
        report.put("policyCount", policyCount);
        
        return report;
    }
    
    public Map<String, Object> getLossRatioReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        BigDecimal totalPremiums = paymentRepository.sumByPaymentType(
                com.insurance.model.PaymentType.PREMIUM
        );
        BigDecimal totalClaims = claimRepository.sumApprovedClaims();
        
        report.put("totalPremiums", totalPremiums != null ? totalPremiums : BigDecimal.ZERO);
        report.put("totalClaims", totalClaims != null ? totalClaims : BigDecimal.ZERO);
        
        if (totalPremiums != null && totalPremiums.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal lossRatio = totalClaims.divide(totalPremiums, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            report.put("lossRatio", lossRatio);
        } else {
            report.put("lossRatio", BigDecimal.ZERO);
        }
        
        return report;
    }
    
    public Map<String, Object> getPremiumDynamics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        var payments = paymentRepository.findByDateRange(startDate, endDate);
        
        BigDecimal totalPremiums = payments.stream()
                .filter(p -> p.getPaymentType().name().equals("PREMIUM"))
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        report.put("totalPremiums", totalPremiums);
        report.put("paymentCount", payments.size());
        
        return report;
    }
    
    public Map<String, Object> getClaimStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        var claims = claimRepository.findByIncidentDateRange(startDate, endDate);
        
        report.put("totalClaims", claims.size());
        
        BigDecimal totalAmount = claims.stream()
                .map(c -> c.getClaimAmount() != null ? c.getClaimAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        report.put("totalClaimAmount", totalAmount);
        
        return report;
    }
    
    public Map<String, Object> getAgentPerformance(Long agentId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        report.put("agentId", agentId);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        var policies = policyRepository.findByAgentId(agentId);
        
        BigDecimal totalPremiums = policies.stream()
                .map(p -> p.getPremium() != null ? p.getPremium() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        report.put("policyCount", policies.size());
        report.put("totalPremiums", totalPremiums);
        
        return report;
    }
}

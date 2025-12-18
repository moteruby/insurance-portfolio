package com.insurance.service;

import com.insurance.exception.DuplicateResourceException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.Agent;
import com.insurance.model.AgentStatus;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentService {
    
    private final AgentRepository agentRepository;
    private final PolicyRepository policyRepository;
    
    public List<Agent> getAllAgents(String branch, AgentStatus status) {
        if (branch != null && status != null) {
            return agentRepository.findByBranchAndStatus(branch, status);
        } else if (branch != null) {
            return agentRepository.findByBranch(branch);
        } else if (status != null) {
            return agentRepository.findByStatus(status);
        }
        return agentRepository.findAll();
    }
    
    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));
    }
    
    public Agent createAgent(Agent agent) {
        // Проверка на дубликат email
        if (agent.getEmail() != null && agentRepository.findByEmail(agent.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Agent", "email", agent.getEmail());
        }
        
        // Установка начального статуса
        if (agent.getStatus() == null) {
            agent.setStatus(AgentStatus.ACTIVE);
        }
        
        return agentRepository.save(agent);
    }
    
    public Agent updateAgent(Long id, Agent agentDetails) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));
        
        // Проверка на дубликат email (если email изменился)
        if (agentDetails.getEmail() != null && !agentDetails.getEmail().equals(agent.getEmail())) {
            if (agentRepository.findByEmail(agentDetails.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Agent", "email", agentDetails.getEmail());
            }
        }
        
        if (agentDetails.getFullName() != null) agent.setFullName(agentDetails.getFullName());
        if (agentDetails.getEmail() != null) agent.setEmail(agentDetails.getEmail());
        if (agentDetails.getPhone() != null) agent.setPhone(agentDetails.getPhone());
        if (agentDetails.getBranch() != null) agent.setBranch(agentDetails.getBranch());
        if (agentDetails.getHireDate() != null) agent.setHireDate(agentDetails.getHireDate());
        if (agentDetails.getStatus() != null) agent.setStatus(agentDetails.getStatus());
        if (agentDetails.getCommissionRate() != null) agent.setCommissionRate(agentDetails.getCommissionRate());
        
        return agentRepository.save(agent);
    }
    
    public void deleteAgent(Long id) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));
        agentRepository.delete(agent);
    }
    
    public Map<String, Object> getAgentStatistics(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", agentId));
        
        Map<String, Object> statistics = new HashMap<>();
        
        // Количество полисов агента
        long policyCount = policyRepository.findByAgentId(agentId).size();
        statistics.put("agentId", agentId);
        statistics.put("agentName", agent.getFullName());
        statistics.put("branch", agent.getBranch());
        statistics.put("policyCount", policyCount);
        statistics.put("status", agent.getStatus());
        statistics.put("commissionRate", agent.getCommissionRate());
        
        // Расчет общей суммы премий
        BigDecimal totalPremiums = policyRepository.findByAgentId(agentId).stream()
                .map(policy -> policy.getPremium() != null ? policy.getPremium() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("totalPremiums", totalPremiums);
        
        // Расчет комиссии
        BigDecimal commission = totalPremiums.multiply(
                agent.getCommissionRate() != null ? agent.getCommissionRate() : BigDecimal.ZERO
        );
        statistics.put("totalCommission", commission);
        
        return statistics;
    }
    
    public List<Agent> searchAgents(String searchTerm) {
        return agentRepository.searchByName(searchTerm);
    }
}

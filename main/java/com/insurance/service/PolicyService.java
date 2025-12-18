package com.insurance.service;

import com.insurance.dto.PolicyDTO;
import com.insurance.exception.DuplicateResourceException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.*;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.ClientRepository;
import com.insurance.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyService {
    
    private final PolicyRepository policyRepository;
    private final ClientRepository clientRepository;
    private final AgentRepository agentRepository;
    
    public List<PolicyDTO> getAllPolicies(InsuranceType insuranceType, PolicyStatus status) {
        List<Policy> policies;
        
        if (insuranceType != null && status != null) {
            policies = policyRepository.findAll().stream()
                    .filter(p -> p.getInsuranceType() == insuranceType && p.getStatus() == status)
                    .collect(Collectors.toList());
        } else if (insuranceType != null) {
            policies = policyRepository.findByInsuranceType(insuranceType);
        } else if (status != null) {
            policies = policyRepository.findByStatus(status);
        } else {
            policies = policyRepository.findAll();
        }
        
        return policies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PolicyDTO getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", id));
        return convertToDTO(policy);
    }
    
    public PolicyDTO createPolicy(PolicyDTO policyDTO) {
        // Проверка на дубликат номера полиса
        if (policyDTO.getPolicyNumber() != null && 
            policyRepository.findByPolicyNumber(policyDTO.getPolicyNumber()).isPresent()) {
            throw new DuplicateResourceException("Policy", "policyNumber", policyDTO.getPolicyNumber());
        }
        
        Policy policy = convertToEntity(policyDTO);
        
        // Автоматическое определение статуса
        policy.setStatus(determineStatus(policy));
        
        Policy savedPolicy = policyRepository.save(policy);
        return convertToDTO(savedPolicy);
    }
    
    public PolicyDTO updatePolicy(Long id, PolicyDTO policyDTO) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", id));
        
        // Проверка на дубликат номера полиса (если номер изменился)
        if (policyDTO.getPolicyNumber() != null && 
            !policyDTO.getPolicyNumber().equals(policy.getPolicyNumber())) {
            if (policyRepository.findByPolicyNumber(policyDTO.getPolicyNumber()).isPresent()) {
                throw new DuplicateResourceException("Policy", "policyNumber", policyDTO.getPolicyNumber());
            }
        }
        
        updatePolicyFromDTO(policy, policyDTO);
        
        // Автоматическое обновление статуса
        policy.setStatus(determineStatus(policy));
        
        Policy updatedPolicy = policyRepository.save(policy);
        return convertToDTO(updatedPolicy);
    }
    
    public void deletePolicy(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", id));
        policyRepository.delete(policy);
    }
    
    public List<PolicyDTO> getExpiringPolicies(int daysAhead) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysAhead);
        
        List<Policy> policies = policyRepository.findExpiringPolicies(startDate, endDate);
        return policies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public void updateExpiredPolicies() {
        List<Policy> expiredPolicies = policyRepository.findExpiredActivePolicies(LocalDate.now());
        expiredPolicies.forEach(policy -> {
            policy.setStatus(PolicyStatus.EXPIRED);
            policyRepository.save(policy);
        });
    }
    
    private PolicyStatus determineStatus(Policy policy) {
        LocalDate now = LocalDate.now();
        
        if (policy.getEndDate().isBefore(now)) {
            return PolicyStatus.EXPIRED;
        } else if (policy.getStartDate().isAfter(now)) {
            return PolicyStatus.SUSPENDED;
        } else {
            return PolicyStatus.ACTIVE;
        }
    }
    
    private PolicyDTO convertToDTO(Policy policy) {
        PolicyDTO dto = new PolicyDTO();
        dto.setId(policy.getId());
        dto.setPolicyNumber(policy.getPolicyNumber());
        dto.setInsuranceType(policy.getInsuranceType());
        dto.setInsuredAmount(policy.getInsuredAmount());
        dto.setPremium(policy.getPremium());
        dto.setStartDate(policy.getStartDate());
        dto.setEndDate(policy.getEndDate());
        dto.setStatus(policy.getStatus());
        
        if (policy.getClient() != null) {
            dto.setClientId(policy.getClient().getId());
            dto.setClientName(policy.getClient().getFullName());
        }
        
        if (policy.getAgent() != null) {
            dto.setAgentId(policy.getAgent().getId());
            dto.setAgentName(policy.getAgent().getFullName());
        }
        
        return dto;
    }
    
    private Policy convertToEntity(PolicyDTO dto) {
        Policy policy = new Policy();
        policy.setPolicyNumber(dto.getPolicyNumber());
        policy.setInsuranceType(dto.getInsuranceType());
        policy.setInsuredAmount(dto.getInsuredAmount());
        policy.setPremium(dto.getPremium());
        policy.setStartDate(dto.getStartDate());
        policy.setEndDate(dto.getEndDate());
        
        // Привязка к клиенту
        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", "id", dto.getClientId()));
            policy.setClient(client);
        }
        
        // Привязка к агенту
        if (dto.getAgentId() != null) {
            Agent agent = agentRepository.findById(dto.getAgentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", dto.getAgentId()));
            policy.setAgent(agent);
        }
        
        return policy;
    }
    
    private void updatePolicyFromDTO(Policy policy, PolicyDTO dto) {
        if (dto.getPolicyNumber() != null) policy.setPolicyNumber(dto.getPolicyNumber());
        if (dto.getInsuranceType() != null) policy.setInsuranceType(dto.getInsuranceType());
        if (dto.getInsuredAmount() != null) policy.setInsuredAmount(dto.getInsuredAmount());
        if (dto.getPremium() != null) policy.setPremium(dto.getPremium());
        if (dto.getStartDate() != null) policy.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) policy.setEndDate(dto.getEndDate());
        
        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", "id", dto.getClientId()));
            policy.setClient(client);
        }
        
        if (dto.getAgentId() != null) {
            Agent agent = agentRepository.findById(dto.getAgentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", dto.getAgentId()));
            policy.setAgent(agent);
        }
    }
}

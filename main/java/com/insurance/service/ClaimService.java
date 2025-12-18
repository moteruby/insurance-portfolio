package com.insurance.service;

import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.Claim;
import com.insurance.model.ClaimStatus;
import com.insurance.model.Policy;
import com.insurance.repository.ClaimRepository;
import com.insurance.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClaimService {
    
    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    
    public List<Claim> getAllClaims(ClaimStatus status) {
        if (status != null) {
            return claimRepository.findByStatus(status);
        }
        return claimRepository.findAll();
    }
    
    public Claim getClaimById(Long id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", id));
    }
    
    public Claim createClaim(Claim claim) {
        // Проверка существования полиса
        if (claim.getPolicy() != null && claim.getPolicy().getId() != null) {
            Policy policy = policyRepository.findById(claim.getPolicy().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", claim.getPolicy().getId()));
            claim.setPolicy(policy);
        }
        
        // Установка начального статуса
        if (claim.getStatus() == null) {
            claim.setStatus(ClaimStatus.REGISTERED);
        }
        
        return claimRepository.save(claim);
    }
    
    public Claim updateClaim(Long id, Claim claimDetails) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", id));
        
        if (claimDetails.getClaimNumber() != null) claim.setClaimNumber(claimDetails.getClaimNumber());
        if (claimDetails.getIncidentDate() != null) claim.setIncidentDate(claimDetails.getIncidentDate());
        if (claimDetails.getClaimAmount() != null) claim.setClaimAmount(claimDetails.getClaimAmount());
        if (claimDetails.getStatus() != null) claim.setStatus(claimDetails.getStatus());
        if (claimDetails.getDescription() != null) claim.setDescription(claimDetails.getDescription());
        if (claimDetails.getResolutionDate() != null) claim.setResolutionDate(claimDetails.getResolutionDate());
        
        return claimRepository.save(claim);
    }
    
    public void deleteClaim(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", id));
        claimRepository.delete(claim);
    }
    
    public List<Claim> getClaimsByDateRange(LocalDate startDate, LocalDate endDate) {
        return claimRepository.findByIncidentDateRange(startDate, endDate);
    }
    
    public BigDecimal getTotalApprovedClaims() {
        BigDecimal total = claimRepository.sumApprovedClaims();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public long getClaimCountByStatus(ClaimStatus status) {
        return claimRepository.countByStatus(status);
    }
}

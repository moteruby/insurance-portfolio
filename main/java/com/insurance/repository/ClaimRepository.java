package com.insurance.repository;

import com.insurance.model.Claim;
import com.insurance.model.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    
    List<Claim> findByPolicyId(Long policyId);
    
    List<Claim> findByStatus(ClaimStatus status);
    
    @Query("SELECT c FROM Claim c WHERE c.incidentDate BETWEEN :startDate AND :endDate")
    List<Claim> findByIncidentDateRange(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(c.claimAmount) FROM Claim c WHERE c.status = 'APPROVED'")
    BigDecimal sumApprovedClaims();
    
    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = :status")
    long countByStatus(@Param("status") ClaimStatus status);
}

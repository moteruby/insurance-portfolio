package com.insurance.repository;

import com.insurance.model.Policy;
import com.insurance.model.PolicyStatus;
import com.insurance.model.InsuranceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    
    List<Policy> findByInsuranceType(InsuranceType insuranceType);
    
    List<Policy> findByStatus(PolicyStatus status);
    
    List<Policy> findByClientId(Long clientId);
    
    List<Policy> findByAgentId(Long agentId);
    
    Optional<Policy> findByPolicyNumber(String policyNumber);
    
    @Query("SELECT p FROM Policy p WHERE p.endDate BETWEEN :startDate AND :endDate")
    List<Policy> findExpiringPolicies(@Param("startDate") LocalDate startDate, 
                                      @Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM Policy p WHERE p.status = 'ACTIVE' AND p.endDate < :currentDate")
    List<Policy> findExpiredActivePolicies(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT COUNT(p) FROM Policy p WHERE p.status = :status")
    long countByStatus(@Param("status") PolicyStatus status);
}

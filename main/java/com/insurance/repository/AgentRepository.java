package com.insurance.repository;

import com.insurance.model.Agent;
import com.insurance.model.AgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    
    List<Agent> findByBranch(String branch);
    
    List<Agent> findByStatus(AgentStatus status);
    
    List<Agent> findByBranchAndStatus(String branch, AgentStatus status);
    
    Optional<Agent> findByEmail(String email);
    
    @Query("SELECT a FROM Agent a WHERE LOWER(a.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Agent> searchByName(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(a) FROM Agent a WHERE a.status = :status")
    long countByStatus(@Param("status") AgentStatus status);
}

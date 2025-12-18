package com.insurance.repository;

import com.insurance.model.Client;
import com.insurance.model.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    List<Client> findByRegion(String region);
    
    List<Client> findByStatus(ClientStatus status);
    
    List<Client> findByRegionAndStatus(String region, ClientStatus status);
    
    Optional<Client> findByEmail(String email);
    
    @Query("SELECT c FROM Client c WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Client> searchByName(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(c) FROM Client c WHERE c.status = :status")
    long countByStatus(@Param("status") ClientStatus status);
}

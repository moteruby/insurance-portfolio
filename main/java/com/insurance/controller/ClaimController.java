package com.insurance.controller;

import com.insurance.model.Claim;
import com.insurance.model.ClaimStatus;
import com.insurance.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
@Tag(name = "Страховые случаи", description = "API для управления страховыми случаями и выплатами")
public class ClaimController {
    
    private final ClaimService claimService;

    @Operation(summary = "Получить список всех страховых случаев")
    @GetMapping
    public ResponseEntity<List<Claim>> getAllClaims(
            @Parameter(description = "Статус страхового случая") @RequestParam(required = false) ClaimStatus status) {
        List<Claim> claims = claimService.getAllClaims(status);
        return ResponseEntity.ok(claims);
    }

    @Operation(summary = "Получить страховой случай по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Claim> getClaimById(@PathVariable Long id) {
        Claim claim = claimService.getClaimById(id);
        return ResponseEntity.ok(claim);
    }

    @Operation(summary = "Зарегистрировать новый страховой случай")
    @PostMapping
    public ResponseEntity<Claim> createClaim(@Valid @RequestBody Claim claim) {
        Claim createdClaim = claimService.createClaim(claim);
        return ResponseEntity.status(201).body(createdClaim);
    }

    @Operation(summary = "Обновить страховой случай")
    @PutMapping("/{id}")
    public ResponseEntity<Claim> updateClaim(
            @PathVariable Long id,
            @Valid @RequestBody Claim claim) {
        Claim updatedClaim = claimService.updateClaim(id, claim);
        return ResponseEntity.ok(updatedClaim);
    }

    @Operation(summary = "Удалить страховой случай")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}

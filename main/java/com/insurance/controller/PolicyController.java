package com.insurance.controller;

import com.insurance.dto.PolicyDTO;
import com.insurance.model.InsuranceType;
import com.insurance.model.PolicyStatus;
import com.insurance.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Tag(name = "Полисы", description = "API для управления страховыми полисами")
public class PolicyController {
    
    private final PolicyService policyService;

    @Operation(
        summary = "Получить список всех полисов",
        description = "Возвращает список всех страховых полисов с возможностью фильтрации по различным параметрам"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список полисов успешно получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public ResponseEntity<List<PolicyDTO>> getAllPolicies(
        @Parameter(description = "Вид страхования") @RequestParam(required = false) InsuranceType insuranceType,
        @Parameter(description = "Статус полиса") @RequestParam(required = false) PolicyStatus status
    ) {
        List<PolicyDTO> policies = policyService.getAllPolicies(insuranceType, status);
        return ResponseEntity.ok(policies);
    }

    @Operation(summary = "Получить полис по ID")
    @GetMapping("/{id}")
    public ResponseEntity<PolicyDTO> getPolicyById(@PathVariable Long id) {
        PolicyDTO policy = policyService.getPolicyById(id);
        return ResponseEntity.ok(policy);
    }

    @Operation(summary = "Создать новый полис")
    @PostMapping
    public ResponseEntity<PolicyDTO> createPolicy(@Valid @RequestBody PolicyDTO policyDTO) {
        PolicyDTO createdPolicy = policyService.createPolicy(policyDTO);
        return ResponseEntity.status(201).body(createdPolicy);
    }

    @Operation(summary = "Обновить полис")
    @PutMapping("/{id}")
    public ResponseEntity<PolicyDTO> updatePolicy(
            @PathVariable Long id,
            @Valid @RequestBody PolicyDTO policyDTO) {
        PolicyDTO updatedPolicy = policyService.updatePolicy(id, policyDTO);
        return ResponseEntity.ok(updatedPolicy);
    }

    @Operation(summary = "Удалить полис")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить истекающие полисы")
    @GetMapping("/expiring")
    public ResponseEntity<List<PolicyDTO>> getExpiringPolicies(
            @Parameter(description = "Количество дней вперед") @RequestParam(defaultValue = "30") int daysAhead) {
        List<PolicyDTO> policies = policyService.getExpiringPolicies(daysAhead);
        return ResponseEntity.ok(policies);
    }
}

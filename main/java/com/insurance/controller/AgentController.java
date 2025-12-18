package com.insurance.controller;

import com.insurance.model.Agent;
import com.insurance.model.AgentStatus;
import com.insurance.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
@Tag(name = "Агенты", description = "API для управления страховыми агентами")
public class AgentController {
    
    private final AgentService agentService;

    @Operation(
        summary = "Получить список всех агентов",
        description = "Возвращает список всех страховых агентов"
    )
    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents(
        @Parameter(description = "Филиал") @RequestParam(required = false) String branch,
        @Parameter(description = "Статус агента") @RequestParam(required = false) AgentStatus status
    ) {
        List<Agent> agents = agentService.getAllAgents(branch, status);
        return ResponseEntity.ok(agents);
    }

    @Operation(summary = "Получить агента по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Long id) {
        Agent agent = agentService.getAgentById(id);
        return ResponseEntity.ok(agent);
    }

    @Operation(
        summary = "Создать нового агента",
        description = "Регистрация нового страхового агента. Доступно только администраторам."
    )
    @PostMapping
    public ResponseEntity<Agent> createAgent(@Valid @RequestBody Agent agent) {
        Agent createdAgent = agentService.createAgent(agent);
        return ResponseEntity.status(201).body(createdAgent);
    }

    @Operation(summary = "Обновить данные агента")
    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(
            @PathVariable Long id,
            @Valid @RequestBody Agent agent) {
        Agent updatedAgent = agentService.updateAgent(id, agent);
        return ResponseEntity.ok(updatedAgent);
    }

    @Operation(summary = "Удалить агента")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Получить статистику агента",
        description = "Возвращает показатели эффективности агента: количество полисов, сумма премий, убыточность"
    )
    @GetMapping("/{id}/statistics")
    public ResponseEntity<Map<String, Object>> getAgentStatistics(@PathVariable Long id) {
        Map<String, Object> statistics = agentService.getAgentStatistics(id);
        return ResponseEntity.ok(statistics);
    }
}

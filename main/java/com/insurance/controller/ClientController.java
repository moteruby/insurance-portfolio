package com.insurance.controller;

import com.insurance.dto.ClientDTO;
import com.insurance.model.ClientStatus;
import com.insurance.model.Policy;
import com.insurance.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Клиенты", description = "API для управления клиентами страховой компании")
public class ClientController {
    
    private final ClientService clientService;

    @Operation(
        summary = "Получить список всех клиентов",
        description = "Возвращает список всех зарегистрированных клиентов с возможностью фильтрации"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешно получен список клиентов",
            content = @Content(schema = @Schema(implementation = ClientDTO.class))),
        @ApiResponse(responseCode = "401", description = "Не авторизован"),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients(
        @Parameter(description = "Регион для фильтрации") @RequestParam(required = false) String region,
        @Parameter(description = "Статус клиента") @RequestParam(required = false) ClientStatus status
    ) {
        List<ClientDTO> clients = clientService.getAllClients(region, status);
        return ResponseEntity.ok(clients);
    }

    @Operation(
        summary = "Получить клиента по ID",
        description = "Возвращает детальную информацию о клиенте по его идентификатору"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Клиент найден"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(
        @Parameter(description = "ID клиента", required = true) @PathVariable Long id
    ) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @Operation(
        summary = "Создать нового клиента",
        description = "Регистрация нового клиента в системе. Доступно для агентов и администраторов."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Клиент успешно создан"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные"),
        @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные нового клиента", required = true
        )
        @Valid @RequestBody ClientDTO clientDTO
    ) {
        ClientDTO createdClient = clientService.createClient(clientDTO);
        return ResponseEntity.status(201).body(createdClient);
    }

    @Operation(
        summary = "Обновить данные клиента",
        description = "Обновление информации о существующем клиенте"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Данные клиента обновлены"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
        @Parameter(description = "ID клиента", required = true) @PathVariable Long id,
        @Valid @RequestBody ClientDTO clientDTO
    ) {
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }

    @Operation(
        summary = "Удалить клиента",
        description = "Удаление клиента из системы. Доступно только администраторам."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Клиент успешно удален"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден"),
        @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
        @Parameter(description = "ID клиента", required = true) @PathVariable Long id
    ) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Получить историю полисов клиента",
        description = "Возвращает все полисы, оформленные для данного клиента"
    )
    @GetMapping("/{id}/policies")
    public ResponseEntity<List<Policy>> getClientPolicies(
        @Parameter(description = "ID клиента", required = true) @PathVariable Long id
    ) {
        List<Policy> policies = clientService.getClientPolicies(id);
        return ResponseEntity.ok(policies);
    }
}

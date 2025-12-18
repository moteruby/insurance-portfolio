package com.insurance.controller;

import com.insurance.model.User;
import com.insurance.model.UserRole;
import com.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями системы (доступно администраторам)")
public class UserController {
    
    private final UserService userService;

    @Operation(
        summary = "Получить список всех пользователей",
        description = "Возвращает список всех пользователей системы"
    )
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
        @Parameter(description = "Роль пользователя") @RequestParam(required = false) UserRole role,
        @Parameter(description = "Статус активности") @RequestParam(required = false) Boolean active
    ) {
        List<User> users = userService.getAllUsers(role, active);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Создать нового пользователя",
        description = "Регистрация нового пользователя с назначением роли"
    )
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Operation(
        summary = "Обновить пользователя",
        description = "Обновление данных пользователя, изменение роли"
    )
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
        summary = "Удалить пользователя",
        description = "Удаление пользователя из системы"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Изменить пароль пользователя",
        description = "Смена пароля для пользователя"
    )
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
        @PathVariable Long id,
        @Parameter(description = "Новый пароль") @RequestParam String newPassword
    ) {
        userService.changePassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Активировать/деактивировать пользователя",
        description = "Изменение статуса активности пользователя"
    )
    @PostMapping("/{id}/toggle-active")
    public ResponseEntity<User> toggleUserActive(@PathVariable Long id) {
        User user = userService.toggleUserActive(id);
        return ResponseEntity.ok(user);
    }
}

package com.insurance.controller;

import com.insurance.model.User;
import com.insurance.security.JwtUtil;
import com.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для авторизации и управления сессиями")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Operation(
        summary = "Вход в систему",
        description = "Аутентификация пользователя и получение JWT токена"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешная авторизация",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username, request.password)
            );

            User user = userService.getUserByUsername(request.username);
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
            
            // Update last login time
            userService.updateLastLogin(user.getUsername());

            return ResponseEntity.ok(new AuthResponse(
                token, 
                user.getRole().name(),
                user.getId(),
                user.getUsername(),
                user.getFullName()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Неверные учетные данные"));
        }
    }

    @Operation(
        summary = "Выход из системы",
        description = "Завершение сессии пользователя (клиент должен удалить токен)"
    )
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Выход выполнен успешно"));
    }

    @Operation(
        summary = "Получить информацию о текущем пользователе",
        description = "Возвращает данные авторизованного пользователя"
    )
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Пользователь не авторизован"));
        }

        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        
        return ResponseEntity.ok(new UserInfoResponse(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getEmail(),
            user.getRole().name(),
            user.isActive()
        ));
    }

    @Operation(
        summary = "Проверить валидность токена",
        description = "Проверяет, является ли JWT токен действительным"
    )
    @GetMapping("/validate")
    public ResponseEntity<MessageResponse> validateToken() {
        return ResponseEntity.ok(new MessageResponse("Токен действителен"));
    }

    // DTOs
    @Schema(description = "Запрос на авторизацию")
    public static class LoginRequest {
        @Schema(description = "Имя пользователя", example = "admin", required = true)
        public String username;
        
        @Schema(description = "Пароль", example = "admin123", required = true)
        public String password;
    }

    @Schema(description = "Ответ с токеном авторизации")
    public static class AuthResponse {
        @Schema(description = "JWT токен")
        public String token;
        
        @Schema(description = "Роль пользователя")
        public String role;

        @Schema(description = "ID пользователя")
        public Long userId;

        @Schema(description = "Имя пользователя")
        public String username;

        @Schema(description = "Полное имя")
        public String fullName;

        public AuthResponse(String token, String role, Long userId, String username, String fullName) {
            this.token = token;
            this.role = role;
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
        }
    }

    @Schema(description = "Информация о пользователе")
    public static class UserInfoResponse {
        @Schema(description = "ID пользователя")
        public Long id;

        @Schema(description = "Имя пользователя")
        public String username;

        @Schema(description = "Полное имя")
        public String fullName;

        @Schema(description = "Email")
        public String email;

        @Schema(description = "Роль")
        public String role;

        @Schema(description = "Активен")
        public boolean active;

        public UserInfoResponse(Long id, String username, String fullName, String email, String role, boolean active) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.active = active;
        }
    }

    @Schema(description = "Сообщение об ошибке")
    public static class ErrorResponse {
        @Schema(description = "Сообщение об ошибке")
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }

    @Schema(description = "Сообщение")
    public static class MessageResponse {
        @Schema(description = "Сообщение")
        public String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }
}

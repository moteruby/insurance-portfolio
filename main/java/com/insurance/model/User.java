package com.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@Schema(description = "Пользователь системы")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;
    
    @Schema(description = "Имя пользователя", example = "admin", required = true)
    @Column(unique = true, nullable = false)
    private String username;
    
    @Schema(description = "Пароль (хешированный)", required = true)
    @Column(nullable = false)
    private String password;
    
    @Schema(description = "ФИО", example = "Администратор Системы")
    private String fullName;
    
    @Schema(description = "Email", example = "admin@insurance.com")
    private String email;
    
    @Schema(description = "Роль пользователя", example = "ADMIN")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    @Schema(description = "Активен ли пользователь", example = "true")
    private Boolean active;
    
    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;
    
    @Schema(description = "Дата последнего входа")
    private LocalDateTime lastLogin;
}

package com.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "clients")
@Schema(description = "Клиент страховой компании")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор клиента", example = "1")
    private Long id;
    
    @Schema(description = "ФИО клиента", example = "Иванов Иван Иванович", required = true)
    @Column(nullable = false)
    private String fullName;
    
    @Schema(description = "Дата рождения", example = "1985-05-15")
    private LocalDate birthDate;
    
    @Schema(description = "Паспортные данные", example = "1234 567890")
    private String passportData;
    
    @Schema(description = "Адрес регистрации", example = "г. Москва, ул. Ленина, д. 10")
    private String address;
    
    @Schema(description = "Регион", example = "Москва")
    private String region;
    
    @Schema(description = "Телефон", example = "+7 (999) 123-45-67")
    private String phone;
    
    @Schema(description = "Email", example = "ivanov@example.com")
    private String email;
    
    @Schema(description = "Статус клиента", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private ClientStatus status;
    
    @Schema(description = "Дата регистрации")
    private LocalDate registrationDate;
}

package com.insurance.dto;

import com.insurance.model.ClientStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "DTO для работы с клиентами")
public class ClientDTO {
    
    @Schema(description = "ID клиента", example = "1")
    private Long id;
    
    @Schema(description = "ФИО клиента", example = "Иванов Иван Иванович", required = true)
    private String fullName;
    
    @Schema(description = "Дата рождения", example = "1985-05-15")
    private LocalDate birthDate;
    
    @Schema(description = "Паспортные данные", example = "1234 567890")
    private String passportData;
    
    @Schema(description = "Адрес", example = "г. Москва, ул. Ленина, д. 10")
    private String address;
    
    @Schema(description = "Регион", example = "Москва")
    private String region;
    
    @Schema(description = "Телефон", example = "+7 (999) 123-45-67")
    private String phone;
    
    @Schema(description = "Email", example = "ivanov@example.com")
    private String email;
    
    @Schema(description = "Статус", example = "ACTIVE")
    private ClientStatus status;
}

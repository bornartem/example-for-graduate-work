package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Login {

    @NotBlank(message = "Логин не должно быть пустое")
    @Size(min = 4, message = "Логин должен содержать минимум 4 символа")
    @Size(min = 32, message = "Логин должен содержать максимум 32 символа")
    @Schema(description = "логин пользователя")
    private String username;

    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Size(min = 16, message = "Пароль должен содержать максимум 16 символов")
    @Schema(description = "пароль пользователя")
    private String password;
}

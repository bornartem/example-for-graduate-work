package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Register {

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

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, message = "Имя должно содержать минимум 2 символа")
    @Size(min = 16, message = "Имя должен содержать максимум 16 символов")
    @Schema(description = "имя пользователя")
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 2, message = "Фамилия должна содержать минимум 2 символа")
    @Size(min = 16, message = "Фамилия должна содержать максимум 16 символов")
    @Schema(description = "фамилия пользователя")
    private String lastName;

    @NotNull
    @Pattern(regexp = "\\\\+7\\\\(\\\\d{3}\\\\)\\\\s\\\\d{3}-\\\\d{2}-\\\\d{2}")
    @Schema(description = "телефон пользователя")
    private String phone;

    @NotNull
    @Schema(description = "роль пользователя")
    private Role role;
}

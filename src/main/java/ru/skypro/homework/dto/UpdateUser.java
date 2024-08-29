package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUser {

    @NotBlank
    @Schema(description = "имя пользователя")
    @Size(min = 3, message = "Имя должно содержать минимум 2 символа")
    @Size(max = 10, message = "Имя не должно превышать более 10 символов")
    private String firstName;

    @NotBlank
    @Schema(description = "фамилия пользователя")
    @Size(min = 3, message = "Фамилия должна содержать минимум 3 символа")
    @Size(max = 10, message = "Фамилия не должна превышать более 10 символов")
    private String lastName;

    @NotNull
    @Schema(description = "телефон пользователя")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}

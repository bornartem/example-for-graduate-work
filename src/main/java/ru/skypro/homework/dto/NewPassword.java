package ru.skypro.homework.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPassword {

    @Schema(description = "текущий пароль")
    @Size(min = 8, max = 16)
    private String currentPassword;

    @Schema(description = "новый пароль")
    @Size(min = 8, max = 16)
    private String newPassword;
}

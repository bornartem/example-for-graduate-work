package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария")
    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 8, max = 64)
    private String text;
}

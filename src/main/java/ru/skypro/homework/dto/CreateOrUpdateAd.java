package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrUpdateAd {

    @Schema(description = "заголовок объявления")
    @NotBlank(message = "Заголовок не может быть пуст")
    @Size(min = 4, max = 32)
    private String title;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000000)
    @Schema(description = "цена объявления")
    private Integer price;

    @Schema(description = "описание объявления")
    @NotBlank(message = "Необходимо заполнить описание")
    @Size(min = 8, max = 64)
    private String description;
}

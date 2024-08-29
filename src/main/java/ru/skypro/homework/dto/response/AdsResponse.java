package ru.skypro.homework.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AdsResponse {

    @Schema(description = "общее количество объявлений")
    private Integer count;

    private List<AdResponse> results;
}

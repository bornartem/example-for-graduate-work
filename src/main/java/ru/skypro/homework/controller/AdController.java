package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.security.CustomUserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.response.AdResponse;
import ru.skypro.homework.mapper.*;
import ru.skypro.homework.service.impl.AdServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

    private final AdServiceImpl adServiceImpl;
    private final AdMapper adMapper;
    private final AdsMapper adsMapper;
    private final ImageMapper imageMapper;
    private final CreateOrUpdateAdMapper createOrUpdateAdMapper;
    private final ExtendedAdMapper extendedAdMapper;

    @Operation(
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    )
            },
            tags = "Объявления"
    )
    @GetMapping
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.status(HttpStatus.OK).body(adsMapper.toAdsResponse(adServiceImpl.getAllAds()));
    }

    @Operation(
            tags = "Объявления",
            summary = "Добавление объявления",
            operationId = "addAd",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(
                                    mediaType = "multipart/form-data",
                                    encoding = @Encoding(
                                            name = "properties",
                                            contentType = "application/json"
                                    )
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = AdResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(hidden = true)
                            )
                    )
            }
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAdWithImage(@RequestPart MultipartFile image,
                                               @RequestPart CreateOrUpdateAd properties,
                                               @AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok().body(
                adMapper.mappingToDto(
                        adServiceImpl.createAd(
                                createOrUpdateAdMapper.toAd(properties),
                                imageMapper.toImage(image), user)));
    }

    @Operation(
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getAds(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(extendedAdMapper.toExtendedDto(adServiceImpl.findById(id)));
    }

    @Operation(
            summary = "Удаление информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            },
            tags = "Объявления"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @adService.findById(#id).user.id")
    public ResponseEntity<?> removeAd(@PathVariable long id,
                                      @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        try {
            adServiceImpl.deleteAd(id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновление информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            },
            tags = "Объявления"
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @adService.findById(#id).user.id")
    public ResponseEntity<?> updateAds(@PathVariable long id,
                                       @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                       @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(adMapper.mappingToDto(adServiceImpl.updateAd(id, createOrUpdateAd)));
    }

    @Operation(
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/me")
    public ResponseEntity<?> getAdsMe(@AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(adsMapper.toAdsResponse(adServiceImpl.getAuthUserAds(userPrincipal.getUser())));
    }

    @Operation(
            summary = "Обновление картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            },
            tags = "Объявления"
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @adService.findById(#id).user.id")
    public ResponseEntity<?> updateImage(@PathVariable long id,
                                         @RequestPart MultipartFile image) {
        return ResponseEntity.ok().body(adMapper.mappingToDto(adServiceImpl.updateImage(id, imageMapper.toImage(image))));
    }
}

package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.security.CustomUserPrincipal;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.mapper.ImageMapper;
import ru.skypro.homework.mapper.UpdateUserMapper;
import ru.skypro.homework.mapper.UserResponseMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.UserService;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ImageMapper imageMapper;
    private final UserResponseMapper userResponseMapper;
    private final UpdateUserMapper updateUserMapper;

    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "ОК",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "string")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Пользователи"
    )
    @PostMapping(path = "set_password", consumes = "application/json")
    public ResponseEntity<?> setPassword(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                         @RequestBody NewPassword newPassword) {
        userService.setPassword(userPrincipal.getUser(), newPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Пользователи"
    )
    @GetMapping(path = "/me")
    public ResponseEntity<?> getInfoByAuthUser(@AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(userResponseMapper.toUserDto(userPrincipal.getUser()));
    }

    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUser.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            },
            tags = "Пользователи"
    )
    @PatchMapping(path = "/me", consumes = "application/json")
    public ResponseEntity<?> updateInfoByAuthUser(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                  @RequestBody UpdateUser updateUser) {
        User user = userService.updateUser(userPrincipal.getUser(), updateUser);
        return ResponseEntity.status(HttpStatus.OK).body(updateUserMapper.toUpdateUserDto(user));
    }

    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
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
            tags = "Пользователи"
    )
    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAvatarByAuthUser(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                    @RequestPart("image") MultipartFile file) {
        Image avatar = imageMapper.toImage(file);
        userService.updateAvatarUser(userPrincipal.getUser(), avatar);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

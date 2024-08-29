package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.config.security.CustomUserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.CommentsMapper;
import ru.skypro.homework.mapper.CreateOrUpdateCommentMapper;
import ru.skypro.homework.service.impl.CommentServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;
    private final CommentMapper commentMapper;
    private final CommentsMapper commentsMapper;
    private final CreateOrUpdateCommentMapper createOrUpdateCommentMapper;

    @Operation(
            summary = "Получение комментариев объявления",
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
            tags = "Комментарии"
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentsMapper.toCommentsResponse(commentServiceImpl.getCommentsForAd(id)));
    }

    @Operation(
            summary = "Добавление комментария к объявлению",
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
            tags = "Комментарии"
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable long id,
                                        @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                        @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(
                commentMapper.mappingToCommentResponse(
                        commentServiceImpl.addComment(id, createOrUpdateCommentMapper.toComment(createOrUpdateComment), userPrincipal)));
    }

    @Operation(
            summary = "Удаление комментария",
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
            tags = "Комментарии"
    )
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @commentService.getCommentById(#commentId).user.id")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long adId,
                                           @PathVariable long commentId,
                                           @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        commentServiceImpl.deleteCommentFromAd(adId, commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Обновление комментария",
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
            tags = "Комментарии"
    )
    @PreAuthorize("hasRole('ADMIN') or #user.user.id == @commentService.getCommentById(#commentId).user.id")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable long adId,
                                           @PathVariable long commentId,
                                           @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                           @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(
                commentMapper.mappingToCommentResponse(
                        commentServiceImpl.updateComment(adId, commentId, createOrUpdateComment)));
    }
}

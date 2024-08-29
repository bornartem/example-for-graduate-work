package ru.skypro.homework.service;

import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.config.security.CustomUserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments();

    @Transactional
    Comment addComment(Long id, Comment comment, CustomUserPrincipal user);

    @Transactional
    void deleteCommentFromAd(Long adId, Long commentId);

    Comment getCommentFromAdById(Ad ad, Long id);

    Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment createOrUpdateComment);

    List<Comment> getCommentsForAd(Long id);
}

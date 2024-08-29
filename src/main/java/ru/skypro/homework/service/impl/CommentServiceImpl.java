package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.config.security.CustomUserPrincipal;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdServiceImpl adServiceImpl;

    @Override
    public List<Comment> getComments() {
        log.info("Получение всех комментариев");
        return commentRepository.findAll();
    }

    @Override
    @Transactional
    public Comment addComment(Long id, Comment comment, CustomUserPrincipal user) {
        log.info("Добавление комментария: {}", comment.getId() +
                ", " + comment.getText() +
                ", " + comment.getCreatedAt() +
                ", " + comment.getUser() +
                " к объявлению " + id);
        comment.setUser(user.getUser());
        comment.setCreatedAt(LocalDateTime.now());
        Comment save = commentRepository.save(comment);
        Ad ad = adServiceImpl.findById(id);
        ad.getComments().add(save);
        adServiceImpl.create(ad);
        return save;
    }

    @Override
    @Transactional
    public void deleteCommentFromAd(Long adId, Long commentId) {
        Ad ad = adServiceImpl.findById(adId);
        Comment comment = getCommentFromAdById(ad, commentId);
        ad.getComments().remove(comment);
        adServiceImpl.create(ad);
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment getCommentFromAdById(Ad ad, Long id) {
        log.info("Получение комментария с id: {}", id + ", объявления с id " + ad.getId());
        return ad.getComments().stream()
                .filter(comment -> comment.getId() == (id))
                .findFirst()
                .orElseThrow(
                        () -> new CommentNotFoundException(
                                "Комментарий с id=" + id + " не найден в объявлении с id=" + ad.getId()));
    }

    @Override
    public Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment createOrUpdateComment) {
        Ad ad = adServiceImpl.findById(adId);
        Comment comment = getCommentFromAdById(ad, commentId);
        comment.setText(createOrUpdateComment.getText());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsForAd(Long id) {
        log.info("Получение всех комментариев объявления: {}", id);
        return adServiceImpl.findById(id).getComments();
    }

    public Comment getCommentById(Long id) {
        log.info("Получение комментария по id: {}", id);
        return commentRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException("Комментария с id=" + id + " не найдено")
        );
    }
}

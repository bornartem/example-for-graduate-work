package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.response.CommentResponse;
import ru.skypro.homework.dto.response.CommentsResponse;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentsMapper {

    private final CommentMapper commentMapper;

    public CommentsResponse toCommentsResponse(List<Comment> comments) {
        List<CommentResponse> responses = comments.stream()
                .map(commentMapper::mappingToCommentResponse)
                .toList();
        CommentsResponse commentsResponse = new CommentsResponse();
        commentsResponse.setResults(responses);
        commentsResponse.setCount(responses.size());
        return commentsResponse;
    }
}

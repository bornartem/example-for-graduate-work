package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CreateOrUpdateCommentMapperTest {

    @Autowired
    private CreateOrUpdateCommentMapper createOrUpdateCommentMapper;

    @Test
    void shouldMapDtoToCommentEntity() {

        CreateOrUpdateComment createOrUpdateComment = CreateOrUpdateComment.builder()
                .text("some text")
                .build();

        Comment comment = createOrUpdateCommentMapper.toComment(createOrUpdateComment);

        assertThat(comment.getText()).isEqualTo(createOrUpdateComment.getText());
    }
}

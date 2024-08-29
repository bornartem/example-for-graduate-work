package ru.skypro.homework.mapper.response;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.response.CommentResponse;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void shouldMapCommentEntityToCommentResponse() {

        Image image = Image.builder()
                .fileSize(1024)
                .mediaType("image/png")
                .data(new byte[]{1, 2, 3, 4})
                .build();

        User user = User.builder()
                .id(2L)
                .username("username")
                .password("password123")
                .firstName("test")
                .lastName("test")
                .phone("+7(999)081-50-52")
                .role(Role.USER)
                .avatar(image)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("some text")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        CommentResponse commentResponse = commentMapper.mappingToCommentResponse(comment);

        assertThat(commentResponse).isNotNull();
        assertThat(commentResponse.getPk()).isEqualTo(1L);
        assertThat(commentResponse.getAuthorImage()).isEqualTo("/image/" + image.getId());
        assertThat(commentResponse.getCreatedAt()).isEqualTo(toEpochMilli(comment.getCreatedAt()));
        assertThat(commentResponse.getAuthor()).isEqualTo(2L);
        assertThat(commentResponse.getAuthorFirstName()).isEqualTo(user.getFirstName());
        assertThat(commentResponse.getText()).isEqualTo(comment.getText());
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }
}

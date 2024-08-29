package ru.skypro.homework.mapper.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.response.CommentResponse;
import ru.skypro.homework.dto.response.CommentsResponse;
import ru.skypro.homework.mapper.CommentsMapper;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentsMapperTest {

    @Autowired
    private CommentsMapper commentsMapper;


    @Test
    void shouldMapAdResponseToAdsResponse() {

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


        CommentResponse response = CommentResponse.builder()
                .author(Math.toIntExact(user.getId()))
                .authorImage("/image/" + user.getAvatar().getId())
                .authorFirstName(user.getFirstName())
                .createdAt(toEpochMilli(comment.getCreatedAt()))
                .pk(Math.toIntExact(comment.getId()))
                .text(comment.getText())
                .build();

        List<Comment> comments = List.of(comment);
        List<CommentResponse> responseList = List.of(response);
        int count = responseList.size();
        CommentsResponse commentsResponse = commentsMapper.toCommentsResponse(comments);

        assertThat(comments.size()).isEqualTo(count);
        assertThat(commentsResponse).isEqualTo(commentsResponse);
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }

}

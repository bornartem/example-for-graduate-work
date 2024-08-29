package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    private static final String COMMENT_URL = "/ads/{id}/comments";
    private static final String USER_JACK = "captain.jack.sparrow@gmail.com";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-bullseye"))
            .withDatabaseName("integration-tests-db");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.liquibase.contexts", () -> "prod,test");
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldReturnAllComments() {
        mockMvc.perform(get(COMMENT_URL, 2))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldPostComment() {
        CreateOrUpdateComment createOrUpdateComment = CreateOrUpdateComment.builder()
                .text("some authenticated user text")
                .build();

        mockMvc.perform(post(COMMENT_URL, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldDeleteComment() {
        mockMvc.perform(delete(COMMENT_URL + "/{commentId}", 1, 1))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldUpdateComment() {
        CreateOrUpdateComment comment = CreateOrUpdateComment.builder()
                .text("some authenticated user text")
                .build();

        mockMvc.perform(patch(COMMENT_URL + "/{commentId}", 2, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk());
    }
}

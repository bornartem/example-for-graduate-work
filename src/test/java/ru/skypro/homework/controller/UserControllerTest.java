package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String SET_PASSWORD_URL = "/users/set_password";
    private static final String USERS_URL = "/users/me";
    private static final String IMAGE_URL = "/users/me/image";
    private static final String USER_JACK = "captain.jack.sparrow@gmail.com";
    private static final String USER_JAMES = "james.norrington@gmail.com";
    private static final String USER_ELIZABETH = "elizabeth.swann@gmail.com";



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
    @WithUserDetails(USER_ELIZABETH)
    void shouldSetPassword() {
        NewPassword newPassword = new NewPassword("GovernorDaughter", "MsrTurner");
        mockMvc.perform(post(SET_PASSWORD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldReturnInfoByAuthUser() {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jack"))
                .andExpect(jsonPath("$.lastName").value("Sparrow"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.email").value("captain.jack.sparrow@gmail.com"))
                .andExpect(jsonPath("$.phone").value("+7 (812) 1234567"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.image").value(startsWith("/image/")));
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JAMES)
    void shouldUpdateUserDetails() {
        UpdateUser updateUser = UpdateUser.builder()
                .firstName("James1")
                .lastName("Norrington1")
                .phone("+7 (154) 4895761")
                .build();

        mockMvc.perform(patch(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updateUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updateUser.getLastName()))
                .andExpect(jsonPath("$.phone").value(updateUser.getPhone()));
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldUpdateUserAvatar() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "avatar.png", MediaType.IMAGE_PNG_VALUE,
                "avatar".getBytes()
        );

        mockMvc.perform(multipart(IMAGE_URL)
                        .file(imageFile)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

}

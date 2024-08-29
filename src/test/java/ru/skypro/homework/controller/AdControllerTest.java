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
import ru.skypro.homework.dto.CreateOrUpdateAd;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class AdControllerTest {

    private static final String USER_JACK = "captain.jack.sparrow@gmail.com";
    private static final String USER_ELIZABETH = "elizabeth.swann@gmail.com";
    private static final String ADS_URL = "/ads";
    private static final String ADS_URL_ID = "/ads/{id}";
    private static final String ADS_ME_URL = "/ads/me";
    private static final String ADS_IMAGE_URL = "/ads/{id}/image";

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
    void shouldReturnAllAds() {
        mockMvc.perform(get(ADS_URL))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_ELIZABETH)
    void shouldPostAdd() {
        MockMultipartFile file = new MockMultipartFile(
                "image", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes()
        );
        String json = "{ \"title\": \"TestTitle\", \"price\": 10, \"description\": \"TestDescription\" }";

        MockMultipartFile properties = new MockMultipartFile(
                "properties", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        mockMvc.perform(multipart(ADS_URL)
                        .file(file)
                        .file(properties)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("TestTitle"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.image").value(startsWith("/image/")))
                .andExpect(jsonPath("$.author").value(5))
                .andExpect(jsonPath("$.pk").exists());
    }

    @Test
    @SneakyThrows
    void shouldReturnUnauthorized() {
        MockMultipartFile file = new MockMultipartFile(
                "image", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes()
        );
        String json = "{ \"title\": \"TestTitle\", \"price\": 10, \"description\": \"TestDescription\" }";

        MockMultipartFile properties = new MockMultipartFile(
                "properties", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        mockMvc.perform(multipart(ADS_URL)
                        .file(file)
                        .file(properties)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldReturnAdById() {
        mockMvc.perform(get(ADS_URL_ID, 1))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldDeleteAdById() {
        mockMvc.perform(delete(ADS_URL_ID, 1))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldUpdateAd() {
        CreateOrUpdateAd ad = CreateOrUpdateAd.builder()
                .title("some title")
                .price(1000)
                .description("some description")
                .build();

        mockMvc.perform(patch(ADS_URL_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ad)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ad.getTitle()))
                .andExpect(jsonPath("$.price").value(ad.getPrice()));
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void shouldReturnAdsByAuthUser() {
        mockMvc.perform(get(ADS_ME_URL))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails(USER_JACK)
    void updateImageAd() {
        MockMultipartFile file = new MockMultipartFile(
                "image", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        mockMvc.perform(multipart(ADS_IMAGE_URL, 1)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());
    }
}

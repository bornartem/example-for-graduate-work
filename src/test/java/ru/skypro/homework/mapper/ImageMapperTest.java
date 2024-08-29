package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import ru.skypro.homework.model.Image;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ImageMapperTest {

    @Autowired
    private ImageMapper imageMapper;

    @Test
    void shouldMapToImage() throws IOException {
        byte[] content = {1, 2, 3, 4};
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        Image image = imageMapper.toImage(multipartFile);

        assertThat(image).isNotNull();
        assertThat(image.getData()).isEqualTo(multipartFile.getBytes());
        assertThat(image.getMediaType()).isEqualTo(multipartFile.getContentType());
        assertThat(image.getFileSize()).isEqualTo(multipartFile.getSize());
    }
}

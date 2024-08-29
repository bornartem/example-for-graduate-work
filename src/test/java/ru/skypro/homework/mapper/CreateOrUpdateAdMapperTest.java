package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CreateOrUpdateAdMapperTest {

    @Autowired
    private CreateOrUpdateAdMapper createOrUpdateAdMapper;

    @Test
    void shouldMapDtoToEntityAd() {

        CreateOrUpdateAd createOrUpdateAd = CreateOrUpdateAd.builder()
                .title("some title")
                .price(1000)
                .description("some description")
                .build();

        Ad ad = createOrUpdateAdMapper.toAd(createOrUpdateAd);

        assertThat(ad.getTitle()).isEqualTo(createOrUpdateAd.getTitle());
        assertThat(ad.getPrice()).isEqualTo(createOrUpdateAd.getPrice());
        assertThat(ad.getDescription()).isEqualTo(createOrUpdateAd.getDescription());
    }
}

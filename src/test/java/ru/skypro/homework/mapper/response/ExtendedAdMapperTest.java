package ru.skypro.homework.mapper.response;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.response.ExtendedAdResponse;
import ru.skypro.homework.mapper.ExtendedAdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ExtendedAdMapperTest {

    @Autowired
    private ExtendedAdMapper extendedAdMapper;

    @Test
    void shouldMapEntityToDtoExtendedAd() {

        Image avatar = Image.builder()
                .id(1L)
                .fileSize(1024)
                .mediaType("image/png")
                .data(new byte[]{1, 2, 3, 4})
                .build();

        User user = User.builder()
                .id(2L)
                .username("testuser")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .phone("1234567890")
                .role(Role.USER)
                .avatar(avatar)
                .build();

        Ad ad = new Ad();
        ad.setId(3L);
        ad.setPrice(100);
        ad.setTitle("Test Title");
        ad.setDescription("Test Description");
        ad.setImage(avatar);
        ad.setUser(user);

        ExtendedAdResponse extendedAdResponse = extendedAdMapper.toExtendedDto(ad);

        assertThat(extendedAdResponse.getPk()).isEqualTo(ad.getId());
        assertThat(extendedAdResponse.getAuthorFirstName()).isEqualTo(ad.getUser().getFirstName());
        assertThat(extendedAdResponse.getAuthorLastName()).isEqualTo(ad.getUser().getLastName());
        assertThat(extendedAdResponse.getDescription()).isEqualTo(ad.getDescription());
        assertThat(extendedAdResponse.getEmail()).isEqualTo(ad.getUser().getUsername());
        assertThat(extendedAdResponse.getImage()).isEqualTo("/image/" + ad.getImage().getId());
        assertThat(extendedAdResponse.getPhone()).isEqualTo(ad.getUser().getPhone());
        assertThat(extendedAdResponse.getPrice()).isEqualTo(ad.getPrice());
        assertThat(extendedAdResponse.getTitle()).isEqualTo(ad.getTitle());
    }
}

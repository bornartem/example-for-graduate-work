package ru.skypro.homework.mapper.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.mapper.UserResponseMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserResponseMapperTest {

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Test
    void shouldMapFromEntityUserToDto() {

        Image image = Image.builder()
                .mediaType("image/png")
                .fileSize(1024)
                .data(new byte[]{1, 2, 3, 4})
                .build();

        User user = User.builder()
                .id(1L)
                .username("username")
                .password("some password")
                .firstName("some name")
                .lastName("some lastName")
                .phone("+7(999) 123-11-22")
                .avatar(image)
                .role(Role.USER)
                .build();

        UserResponse userResponse = userResponseMapper.toUserDto(user);

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getEmail()).isEqualTo(user.getUsername());
        assertThat(userResponse.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(userResponse.getPhone()).isEqualTo(user.getPhone());
        assertThat(userResponse.getRole()).isEqualTo(user.getRole());
        assertThat(userResponse.getImage()).isEqualTo("/image/" + user.getAvatar().getId());
    }

    @Test
    void shouldMapFromDtoToUserEntity() {

        User user = User.builder()
                .id(1L)
                .username("username")
                .password("some password")
                .firstName("some name")
                .lastName("some lastName")
                .phone("+7(999) 123-11-22")
                .role(Role.USER)
                .build();

        Register register = Register.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
        User entity = userResponseMapper.fromRegister(register);

        assertThat(entity.getUsername()).isEqualTo(user.getUsername());
        assertThat(entity.getPassword()).isEqualTo(user.getPassword());
        assertThat(entity.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(user.getLastName());
        assertThat(entity.getPhone()).isEqualTo(user.getPhone());
        assertThat(entity.getRole()).isEqualTo(user.getRole());

    }
}

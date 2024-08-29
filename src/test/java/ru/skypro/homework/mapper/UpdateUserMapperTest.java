package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UpdateUserMapperTest {

    @Autowired
    private UpdateUserMapper updateUserMapper;

    @Test
    void shouldMapFromEntityToDtoUpdateUser(){

        User user = User.builder()
                .firstName("some name")
                .lastName("some lastName")
                .phone("+7(999) 123-11-22")
                .build();

        UpdateUser updateUser = updateUserMapper.toUpdateUserDto(user);

        assertThat(updateUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(updateUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(updateUser.getPhone()).isEqualTo(user.getPhone());
    }
}

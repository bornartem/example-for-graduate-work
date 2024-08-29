package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring")
public interface UpdateUserMapper {

    UpdateUser toUpdateUserDto(User user);
}

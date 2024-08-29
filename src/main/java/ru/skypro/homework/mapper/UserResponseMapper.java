package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring")
public abstract class UserResponseMapper {

    @Value("${download.url}")
    public String downloadUrl;

    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "image", expression = "java(user.getAvatar() == null ? null : downloadUrl + user.getAvatar().getId())")
    public abstract UserResponse toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    public abstract User fromRegister(Register register);
}

package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.homework.dto.response.ExtendedAdResponse;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public abstract class ExtendedAdMapper {

    @Value("${download.url}")
    protected String downloadUrl;

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "image", expression = "java(entity.getImage() == null ? null : downloadUrl + entity.getImage().getId())")
    @Mapping(target = "phone", source = "user.phone")
    public abstract ExtendedAdResponse toExtendedDto(Ad entity);
}

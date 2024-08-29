package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.homework.dto.response.CommentResponse;
import ru.skypro.homework.model.Comment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Value("${download.url}")
    protected String downloadUrl;

    @Mapping(target = "author", source = "user.id")
    @Mapping(
            target = "authorImage",
            expression = "java(entity.getUser().getAvatar() == null ? null : downloadUrl + entity.getUser().getAvatar().getId())")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "createdAt", expression = "java(toEpochMilli(entity.getCreatedAt()))")
    @Mapping(target = "pk", source = "id")
    public abstract CommentResponse mappingToCommentResponse(Comment entity);

    public static long toEpochMilli(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }
}

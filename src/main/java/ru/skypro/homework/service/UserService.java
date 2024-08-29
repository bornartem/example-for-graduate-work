package ru.skypro.homework.service;

import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

public interface UserService {

    @Transactional
    User save(User user);

    void setPassword(User user, NewPassword password);

    User updateUser(User user, UpdateUser updateUser);

    @Transactional
    User updateAvatarUser(User user, Image avatar);
}

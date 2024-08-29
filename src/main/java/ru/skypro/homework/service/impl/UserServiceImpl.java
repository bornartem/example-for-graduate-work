package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.IncorrectCurrentPasswordException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User findByUserName(String userName) {
        log.info("Поиск пользователя по имени: {}", userName);
        return userRepository.findByUsername(userName).orElseThrow(
                () -> {
                    log.error("Пользователь с именем пользователя {} не найден", userName);
                    return new UsernameNotFoundException(
                            "Нет пользователя с таким именем пользователя \"" + userName + "\""
                    );
                }
        );
    }

    @Override
    @Transactional
    public User save(User user) {
        log.info("Сохранение пользователя: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public void setPassword(User user, NewPassword password) {
        log.info("Изменение пароля для пользователя: {}", user.getUsername());
        if (!encoder.matches(password.getCurrentPassword(), user.getPassword())) {
            log.error("Неверный текущий пароль для пользователя: {}", user.getUsername());
            throw new IncorrectCurrentPasswordException("Текущий пароль неверен");
        }
        user.setPassword(encoder.encode(password.getNewPassword()));
        save(user);
    }

    @Override
    public User updateUser(User user, UpdateUser updateUser) {
        log.info("Обновление данных для пользователя: {}", user.getUsername());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        return save(user);
    }

    @Override
    @Transactional
    public User updateAvatarUser(User user, Image avatar) {
        log.info("Обновление аватара для пользователя: {}", user.getUsername());
        if (user.getAvatar() != null) {
            avatar.setId(user.getAvatar().getId());
        }
        user.setAvatar(avatar);
        return save(user);
    }
}

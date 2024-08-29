package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserResponseMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userServiceImpl;
    private final UserResponseMapper userMapper;
    private final PasswordEncoder encoder;

    @Override
    public boolean login(String userName, String password) {
        try {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            authenticationManager.authenticate(authenticationToken);
            log.info("Авторизация пользователя: {} успешно пройдена", userName);
            return true;
        } catch (AuthenticationException e) {
            log.error("Не удалось войти в систему для пользователя: {}", userName, e);
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        try {
            userDetailsService.loadUserByUsername(register.getUsername());
            log.error(
                    "Регистрация не удалась: пользователь с именем пользователя {} уже существует.",
                    register.getUsername()
            );
            return false;
        } catch (UsernameNotFoundException e) {
            User user = userMapper.fromRegister(register);
            user.setPassword(encoder.encode(register.getPassword()));
            userServiceImpl.save(user);
            log.info(
                    "Пользователь успешно зарегистрирован с именем пользователя: {}",
                    register.getUsername());
            return true;
        }
    }
}

package ru.skypro.homework.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRegister() {
        Register register = new Register();
        register.setUsername("testUser");
        register.setPassword("TestPassword");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+7(999) 123-45-67");
        register.setRole(Role.USER);

        Set<ConstraintViolation<Register>> violations = validator.validate(register);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidUsernameEmpty() {
        Register register = new Register();
        register.setUsername(" ");
        register.setPassword("TestPassword123");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+7(999) 123-45-67");
        register.setRole(Role.USER);

        Set<ConstraintViolation<Register>> violations = validator.validate(register);

        assertFalse(violations.isEmpty());
        assertEquals(7, violations.size());
        assertEquals("Логин не должно быть пустое", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidUsernameTooShort() {
        Register register = new Register();
        register.setUsername("te");
        register.setPassword("TestPassword123");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+7(999) 123-45-67");
        register.setRole(Role.USER);

        Set<ConstraintViolation<Register>> violations = validator.validate(register);

        assertFalse(violations.isEmpty());
        assertEquals(6, violations.size());
        assertEquals("Логин должен содержать минимум 4 символа", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidUsernameTooLong() {
        Register register = new Register();
        register.setUsername("jhcjnaejcnjncjevnjsnv,mxnvm,xvnmrvnmrnvmvnxmrvn");
        register.setPassword("TestPassword123");
        register.setFirstName("Test");
        register.setLastName("testtest");
        register.setPhone("+7(999) 123-45-67");
        register.setRole(Role.USER);

        Set<ConstraintViolation<Register>> violations = validator.validate(register);

        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());
        assertEquals("Логин должен содержать максимум 32 символа", violations.iterator().next().getMessage());
        assertEquals("Логин должен содержать максимум 32 символа", violations.iterator().next().getMessage());
    }


    @Test
    void testInvalidPasswordEmpty() {
        Register register = new Register();
        register.setUsername("testUser");
        register.setPassword("");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+7(999) 123-45-67");
        register.setRole(Role.USER);

        Set<ConstraintViolation<Register>> violations = validator.validate(register);

//        assertFalse(violations.isEmpty());
        assertEquals(7, violations.size());
        assertEquals("Пароль не должен быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPasswordTooShort() {
        Register register = Register.builder()
                .username("testww")
                .phone("+7(999)123-45-67")
                .firstName("Test")
                .lastName("User")
                .password("test")
                .role(Role.USER)
                .build();

        Set<ConstraintViolation<Register>> violations = validator.validate(register);

        assertFalse(violations.isEmpty());
        assertEquals(6, violations.size());
        assertEquals("Пароль должен содержать минимум 8 символов", violations.iterator().next().getMessage());
    }

}

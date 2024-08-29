package ru.skypro.homework.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class NewPasswordTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validPassword() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("P@$$w0rd1");
        newPassword.setNewPassword("NewP@$$w0rd");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

        assertTrue(violations.isEmpty());
    }

    @Test
    void newPasswordIsTooShort() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("P@$$w0rd");
        newPassword.setNewPassword("Neww0rd");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("размер должен находиться в диапазоне от 8 до 16", violations.iterator().next().getMessage());
    }

    @Test
    void newPasswordIsTooLong() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("NewP@$$w0rd");
        newPassword.setNewPassword("NewP@$$$$w0rd1234567890w0rd");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("размер должен находиться в диапазоне от 8 до 16", violations.iterator().next().getMessage());
    }

    @Test
    void currentPasswordIsTooShort() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("P@w0rd1");
        newPassword.setNewPassword("NewP@$$ww");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("размер должен находиться в диапазоне от 8 до 16", violations.iterator().next().getMessage());
    }

    @Test
    void newPasswordTooLong() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("P@$$w0rd1");
        newPassword.setNewPassword("NewP@$$w0rd1234567890");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("размер должен находиться в диапазоне от 8 до 16", violations.iterator().next().getMessage());
    }

}

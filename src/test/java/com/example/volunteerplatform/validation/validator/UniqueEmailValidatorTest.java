package com.example.volunteerplatform.validation.validator;


import com.volunteerplatform.service.UserService;
import com.volunteerplatform.validation.validator.UniqueEmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UniqueEmailValidatorTest {

    private UserService userService;
    private UniqueEmailValidator validator;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        validator = new UniqueEmailValidator(userService);
    }

    @Test
    public void testEmailUnique() {
        String email = "test@example.com";
        when(userService.isEmailUnique(email)).thenReturn(true);

        boolean result = validator.isValid(email, null);
        assertTrue(result);
    }

    @Test
    public void testEmailNotUnique() {
        String email = "duplicate@example.com";
        when(userService.isEmailUnique(email)).thenReturn(false);

        boolean result = validator.isValid(email, null);
        assertFalse(result);
    }

    @Test
    public void testNullEmail() {
        boolean result = validator.isValid(null, null);
        assertTrue(result);
    }
}

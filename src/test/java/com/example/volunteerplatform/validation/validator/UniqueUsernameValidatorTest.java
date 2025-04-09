package com.example.volunteerplatform.validation.validator;


import com.volunteerplatform.service.UserService;
import com.volunteerplatform.validation.validator.UniqueUsernameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UniqueUsernameValidatorTest {

    private UserService userService;
    private UniqueUsernameValidator validator;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        validator = new UniqueUsernameValidator(userService);
    }

    @Test
    public void testUsernameUnique() {
        String username = "uniqueUser";
        when(userService.isUsernameUnique(username)).thenReturn(true);
        boolean result = validator.isValid(username, null);
        assertTrue(result);
    }

    @Test
    public void testUsernameNotUnique() {
        String username = "takenUser";
        when(userService.isUsernameUnique(username)).thenReturn(false);
        boolean result = validator.isValid(username, null);
        assertFalse(result);
    }

    @Test
    public void testNullUsername() {
        boolean result = validator.isValid(null, null);
        assertTrue(result);
    }
}

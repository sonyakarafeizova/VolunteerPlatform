package com.example.volunteerplatform.util;


import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.Level;
public class TestUtils {

    public static User createTestUser(Long id, String fullName,Integer age,Level level, String email) {
        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setAge(age);
        user.setLevel(level);
        user.setEmail(email);

        return user;
    }
}

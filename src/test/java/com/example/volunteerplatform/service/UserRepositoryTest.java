package com.example.volunteerplatform.service;

import com.volunteerplatform.VolunteerPlatformApplication;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Level;
import com.volunteerplatform.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = VolunteerPlatformApplication.class)
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

@Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User("testUser2", "Test Name2", "test2@example.com", 25, Level.JUNIOR, "password123");
        userRepository.save(user);
        System.out.println("User saved: " + user.getId());
    }
}

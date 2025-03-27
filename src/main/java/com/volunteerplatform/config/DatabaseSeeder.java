package com.volunteerplatform.config;

import com.volunteerplatform.data.RoleRepository;
import com.volunteerplatform.data.UserRepository;
import com.volunteerplatform.model.Role;
import com.volunteerplatform.model.User;
import com.volunteerplatform.model.enums.UserRoles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    @Override
    public void run(String... args) {
        System.out.println("DatabaseSeeder is running...");
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role(UserRoles.USER);
            Role modRole = new Role(UserRoles.MODERATOR);
            Role adminRole = new Role(UserRoles.ADMIN);
            roleRepository.saveAll(Set.of(userRole, modRole, adminRole));
          //  System.out.println("✅ Roles added to database.");
        }
    }

    private void seedAdminUser() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByRole(UserRoles.ADMIN).orElseThrow();
            System.out.println("Admin role found: " + adminRole.getRole());

            User admin = new User();
            admin.setUsername("admin");
            admin.setFullName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setAge(30);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(roleRepository.findByRole(UserRoles.ADMIN).orElseThrow()));

            userRepository.save(admin);
           //System.out.println("✅ Admin user added to database.");
        }
    }
}
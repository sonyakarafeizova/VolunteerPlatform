package com.volunteerplatform.data;

import com.volunteerplatform.model.Role;
import com.volunteerplatform.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository <Role, Long> {
        Optional<Role> findByName(UserRoles name);
        }

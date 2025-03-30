package com.volunteerplatform.model;

import com.volunteerplatform.model.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    public Role(UserRoles role) {
        this.role = role;
    }


//    public Role setRole(UserRoles role) {
//        this.role = role;
//        return this;
//    }
}

package com.volunteerplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private Integer age;

    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User() {
    }
    public User(String username, String fullName, String email, int age, Level level, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.level = level;
        this.password = password;
    }


}

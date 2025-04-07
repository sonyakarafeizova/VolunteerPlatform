package com.volunteerplatform.model;

import com.volunteerplatform.model.enums.Level;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
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
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;
    private String profileImage;
    @ManyToMany
    @JoinTable(
            name = "users_favourite_mentoring",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "mentoring_id")
    )
    private Set<Mentoring> favouriteMentorings = new HashSet<>();


    public User() {
        this.roles = new HashSet<>();
    }

    public User setLevel(Level level) {
        this.level = level;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

}

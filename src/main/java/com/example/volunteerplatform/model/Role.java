package com.example.volunteerplatform.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;

@Entity
@Table(name="roles")

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private UserRoles name;
}

package com.volunteerplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="pictures")
@Getter
@Setter
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cause cause;
}

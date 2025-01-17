package com.volunteerplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "causes")
@Getter
@Setter
public class Cause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Level level;
    @Column(name = "video_url")
    private String videoUrl;

    @ManyToOne(optional = false)
    private User author;

    @OneToMany(targetEntity = Comment.class, mappedBy = "cause")
    private Set<Comment> comments;

    @OneToMany(targetEntity = Picture.class, mappedBy = "cause")
    private Set<Picture> pictures;

    public Cause() {
        this.comments=new HashSet<>();
        this.pictures=new HashSet<>();
    }

}

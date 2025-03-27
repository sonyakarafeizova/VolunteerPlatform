package com.volunteerplatform.model;

import com.volunteerplatform.model.enums.Level;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(optional = false)
    private User author;

    @OneToMany(targetEntity = Comment.class, mappedBy = "cause")
    private Set<Comment> comments;

    @OneToMany(targetEntity = Picture.class, mappedBy = "cause")
    private Set<Picture> pictures;

    @Column(nullable = false)
    private LocalDateTime created;

    public Cause() {
        this.comments=new HashSet<>();
        this.pictures=new HashSet<>();
    }

}

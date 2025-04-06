package com.volunteerplatform.model;

import com.volunteerplatform.model.enums.Level;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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


    @OneToMany(targetEntity = Picture.class, mappedBy = "cause")
    private Set<Picture> pictures;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    public Cause() {
        this.pictures=new HashSet<>();
    }
    public Cause setAuthor(User author) {
        this.author = author;
        return this;
    }

}

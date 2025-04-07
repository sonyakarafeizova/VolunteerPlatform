package com.volunteerplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mentoring")
@Getter
@Setter
@NoArgsConstructor
public class Mentoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(targetEntity = Comment.class, mappedBy = "mentoring")
    private Set<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_favourite_mentoring",
            joinColumns = @JoinColumn(name = "mentoring_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> favouriteUsers = new HashSet<>();

    public Mentoring(String title, String description, String level, User author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }


    public void addToFavourites(User user) {
        this.favouriteUsers.add(user);

    }
    public void removeFromFavourites(User user) {
        this.favouriteUsers.remove(user);
        user.getFavouriteMentorings().remove(this);
    }
}

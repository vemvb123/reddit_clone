package com.example.Reddit.clone.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "`user`")  // Enclose the table name in backticks
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    public User(String username) {
        this.username = username;
    }

    private String pathToProfileImage;
    private String pathToWallpaperImage;

    @ToString.Exclude
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    private Boolean otherUsersCanSeePosts = true;
    private Boolean otherUsersCanSeeComments = true;

    @ToString.Exclude
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "toUser")
    private Set<Message> messages;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "fromUser")
    private Set<Message> messagesFrom;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> communities = new HashSet<>();

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community_admin",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> administratorOnCommunities = new HashSet<>();

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community_moderator",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> moderatorOnCommunities = new HashSet<>();

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community_banned",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> bannedFromCommunities = new HashSet<>();

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    Set<User> friends = new HashSet<>();


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    public int hashCode() {
        return getClass().hashCode();
    }
}

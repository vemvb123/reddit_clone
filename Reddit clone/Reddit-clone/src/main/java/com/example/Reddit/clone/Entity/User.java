package com.example.Reddit.clone.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "`user`")  // Enclose the table name in backticks
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;

    public User(String firstname, String lastname, String email, String password, String username, Set<RoleEnum> roles) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
    }

    @JsonIgnore
    @ElementCollection
    private Set<RoleEnum> roles = new HashSet<>();

    private String pathToProfileImage;
    private String pathToWallpaperImage;

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    private Boolean otherUsersCanSeePosts = true;
    private Boolean otherUsersCanSeeComments = true;

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @JsonIgnore
    @OneToMany(mappedBy = "toUser")
    private Set<Message> messages;

    @JsonIgnore
    @OneToMany(mappedBy = "fromUser")
    private Set<Message> messagesFrom;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> communities;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community_admin",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> administratorOnCommunities;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community_moderator",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> moderatorOnCommunities;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_community_banned",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> bannedFromCommunities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<RoleEnum> roles = getRoles();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )

    Set<User> friends = new HashSet<>();

    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return this.email;
    }
    public Set<RoleEnum> getRoles() {
        return this.roles;
    }

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

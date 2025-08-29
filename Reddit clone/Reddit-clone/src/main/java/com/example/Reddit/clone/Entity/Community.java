package com.example.Reddit.clone.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private String communityWallpaper;

    private String communityImage;

    private String description;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private Set<Post> posts;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "communityRequestingToJoin", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Message> requestsToJoinCommunity;



    @Enumerated(EnumType.STRING)
    @Column(name = "community_type")
    private CommunityType communityType;


    @JsonIgnore
    @ManyToMany(mappedBy = "communities", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<User> members = new HashSet<>();


    @JsonIgnore
    @ManyToMany(mappedBy = "administratorOnCommunities", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<User> administrators = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "moderatorOnCommunities", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<User> moderators = new HashSet<>();


    @JsonIgnore
    @ManyToMany(mappedBy = "bannedFromCommunities", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<User> bannedUsers = new HashSet<>();





    // rights of moderators
    private Boolean moderatorCanChangeWallpaper;
    private Boolean moderatorCanChangeCommunityImage;
    private Boolean moderatorCanDeleteOthersPosts;
    private Boolean moderatorCanDeleteOthersComments;
    private Boolean moderatorCanBanUser;
    private Boolean moderatorCanDeleteCommunity;
    private Boolean moderatorCanMakeAnnouncement;
    private Boolean moderatorCanChangeCommunityDescription;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Community community = (Community) o;
        return id != null && Objects.equals(id, community.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

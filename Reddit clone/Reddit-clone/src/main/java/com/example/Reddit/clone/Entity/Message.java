package com.example.Reddit.clone.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private MessageTopic messageTopic;



    private LocalDateTime eventHappendAt;



    //a user can have many messages
    //a message can only be for one user
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private User toUser;

    //a user can have many messages be from it
    //a message from a uuser, can only be from that user
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private User fromUser;



    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private Boolean seen;

    @ManyToOne
    @JoinColumn(name = "community_id_requesting_to_join")
    private Community communityRequestingToJoin;



}

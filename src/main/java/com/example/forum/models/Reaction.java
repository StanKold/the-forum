package com.example.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private int id;

    @JsonIgnore
    @Column(name = "user_id")
    private int userID;

    @JsonIgnore
   // @ManyToOne
    @Column(name = "post_id")
    private int postID;

    @Column (name = "type")
    private String reaction;

    public int getId() {
        return id;
    }

    public int getUser() {
        return userID;
    }

    public int getPost() {
        return postID;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUser(int user) {
        this.userID = user;
    }

    public void setPost(int post) {
        this.postID = post;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}

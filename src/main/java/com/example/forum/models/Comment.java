package com.example.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "post_user_replies")
public class Comment {

  @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private int replyId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @Column(name = "content")
    private String content;

    @JsonIgnore
   @Column(name = "created")
   private LocalDate createdOn;

  public Comment() {
    }

    public int getReplyId() {
        return replyId;
    }

    public User getCreator() {
        return creator;
    }

    public String getContent() {
        return content;
    }

    public void setReplyId(int id) {
        this.replyId = id;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

  @Override
  public String toString() {
    return content + " | author: "+creator.getUsername();
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDate createdOn) {
    this.createdOn = createdOn;
  }
}

package com.example.forum.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "post_tags")
public class PostTags {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_tag_id")
    private int postTagId;

    @Column(name="tag_id")
    private int tagId;

    @Column(name ="post_id")
    private int postId;


    public PostTags() {
    }

    public int getPostTagId() {
        return postTagId;
    }

    public void setPostTagId(int postTagId) {
        this.postTagId = postTagId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostTags postTags = (PostTags) o;
        return tagId == postTags.tagId && postId == postTags.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, postId);
    }
}

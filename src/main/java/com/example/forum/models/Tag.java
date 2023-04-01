package com.example.forum.models;

import jakarta.persistence.*;

@Entity
@Table(name ="tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tag_id")
    private int tagId;
    @Column(name="content")
    private String content;

    public Tag() {
    }

    public int getID() {
        return tagId;
    }

    public void setTagID(int tag_id) {
        this.tagId = tag_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}

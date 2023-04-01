package com.example.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;

public class ReturnPostDTO {

    @JsonIgnore
    private User creator;

    private int id;
    private String title,content,author;
    private int likes;

    @JsonIgnore
    private List<Integer> liked;
    private int dislikes;

    @JsonIgnore
    private List<Integer>disliked;
    private int approval;
    private List<String> comments;
    @JsonIgnore
    private List<Comment> comment;

    @JsonIgnore
    private LocalDate date;

    @JsonIgnore
    private int commentsCount;
    private String tagList;

    private int days_old;

    public ReturnPostDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        this.approval = approval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public int getDays_old() {
        return days_old;
    }

    public void setDays_old(int old) {
        this.days_old = old;
    }

    public List<Integer> getLiked() {
        return liked;
    }

    public void setLiked(List<Integer> liked) {
        this.liked = liked;
    }

    public List<Integer> getDisliked() {
        return disliked;
    }

    public void setDisliked(List<Integer> disliked) {
        this.disliked = disliked;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

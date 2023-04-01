package com.example.forum.services;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;

import java.util.List;

public interface PostTagService {
    void addPostTag(User user, Post post, Tag tag);
    List<String> getTags(int id);
    void removePostTag(User user, Post post, Tag tag);
}

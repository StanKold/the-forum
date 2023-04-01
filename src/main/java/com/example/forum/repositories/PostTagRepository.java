package com.example.forum.repositories;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;

import java.util.List;

public interface PostTagRepository {

    void addPostTag(Post post, Tag tag);
    void removePostTag(Post post, Tag tag);

    List<String> getAllPostTags(int postId);

}

package com.example.forum.services;

import com.example.forum.models.Post;
import com.example.forum.models.PostFilterDto;
import com.example.forum.models.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<Post> getAllPosts();

    Post getPost(int id);

    Post createPost(Post post, User user);

    void delete(int id, User user);

    Post update(int id, Post post, User user);
    List<Post> getUserPosts(int id, Optional<String> title, Optional<String> content,
                            Optional<String> sort);

    List<Post> getTopCommentedPosts();

    List<Post> getMostRecentPosts();

    List<Post> searchPost(Optional<String> title, Optional<String> tag);

    long getCount();
    public boolean checkTitleDuplicate(String title);

    List<Post> getAllPosts(PostFilterDto postFilterDto);
}


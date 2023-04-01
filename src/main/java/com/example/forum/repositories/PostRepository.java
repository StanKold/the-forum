package com.example.forum.repositories;

import com.example.forum.models.Post;
import com.example.forum.models.PostFilterDto;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> getPosts();

    Post getPost(int id);

    List<Post> getUserPosts(int userid, Optional<String> title, Optional<String> content,
                            Optional<String> sort);

    Post createPost(Post post);

    void delete(int id);

    Post update(int post_id, Post post);

    List<Post> getTopCommentedPosts();

   Boolean existTitle(String title);

   List<Post> getMostRecentPosts();

   List<Post> searchPost(Optional<String> title, Optional<String> tag);
    long getCount();

    List<Post> getAllPosts(PostFilterDto postFilterDto);

}

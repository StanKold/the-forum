package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.models.Post;
import com.example.forum.models.PostFilterDto;
import com.example.forum.models.User;
import com.example.forum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class PostServiceImpl implements PostService {
    private final PostRepository repository;
    private final UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public List<Post> getAllPosts() {
           return repository.getPosts();
    }

    @Override
    public Post getPost(int id) {
        return repository.getPost(id);
    }

    @Override
    public Post createPost(Post post, User user) {
        if (user.isBlocked()) {throw new AuthorizationException("Only not blocked users can create posts");}

        if ( repository.existTitle(post.getTitle())) {
            throw new DuplicateEntityException("Post", "title", post.getTitle());}

        try {
            post.setUser_id(user.getId());
            return repository.createPost(post);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void delete(int id, User user) {
        if (!(getPost(id).getUser_id() == user.getId() || user.isAdmin()) || user.isBlocked()) {
            throw new AuthorizationException("Only not blocked author or admin can delete post");
        }
        repository.delete(id);
    }

    @Override
    public Post update(int post_id, Post post, User user) {
        if (repository.getPost(post_id).getUser_id() != user.getId() || user.isBlocked()) {
            throw new AuthorizationException("Only not blocked author can change post");
        }
        return repository.update(post_id, post);
    }
    @Override
    public List<Post> getUserPosts(int id, Optional<String> title, Optional<String> content, Optional<String> sort) {
                return repository.getUserPosts(id,title,content,sort);
    }

    @Override
    public List<Post> getTopCommentedPosts() {
        return repository.getTopCommentedPosts();
    }

    @Override
    public List<Post> getMostRecentPosts() {
        return repository.getMostRecentPosts();
    }

    @Override
    public List<Post> searchPost(Optional<String> title, Optional<String> tag) {
        return repository.searchPost(title,tag);
    }

    @Override
    public long getCount() {
        return repository.getCount();
    }

    @Override
    public boolean checkTitleDuplicate(String title) {
        return repository.existTitle(title);
    }

    @Override
    public List<Post> getAllPosts(PostFilterDto postFilterDto) {
        return repository.getAllPosts(postFilterDto);
    }
}

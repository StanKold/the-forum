package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Comment;
import com.example.forum.models.CommentsFilterDto;
import com.example.forum.models.User;
import com.example.forum.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    public static final String BANNED_USERS_CANNOT_ADD_COMMENTS = "Banned users cannot add comments";
    public static final String COMMENTS_PERMISSIONS = "Only not banned creators and admins can modify or delete comments";
    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Comment> getComments() {
        return repository.getComments();
    }

    @Override
    public Comment getComment(int id) {
        return repository.getComment(id);
    }

    @Override
    public Comment createComment(Comment comment, User user) {
        checkCreatePermission(user);
        comment.setCreator(user);
        return repository.createComment(comment);
    }

    @Override
    public void deleteComment(int id, User user) {
        checkModifyPermissions(id, user);
        repository.delete(id);
    }

    @Override
    public Comment update(int id, Comment comment, User user) {
        checkModifyPermissions(id, user);
        return repository.update(id, comment);
    }

    @Override
    public long getCount() {
        return repository.getCount();
    }

    @Override
    public List<Comment> getFilteredComments(CommentsFilterDto commentsFilterDto) {
        return repository.getFilteredComments(commentsFilterDto);
    }

    @Override
    public List<Comment> getComments(String username, Optional<String> content, Optional<String> sort ){
        return repository.getComments(username, content, sort);
    }

    @Override
    public List<Comment> getCommentsByPostTitle(String title) {
        return repository.getCommentsByPostTitle(title);
    }

    @Override
    public List<Comment> getCommentsByPostId(int postId) {
        return repository.getCommentsByPostId(postId);
    }

    private void checkCreatePermission(User user) {
        if (user.isBlocked()) {
            throw new AuthorizationException(BANNED_USERS_CANNOT_ADD_COMMENTS);
        }
    }

    private void checkModifyPermissions(int commentId, User user) {
        Comment comment = repository.getComment(commentId);
        if (!(user.isAdmin() || comment.getCreator().equals(user)) || user.isBlocked()) {
            throw new AuthorizationException
                    (COMMENTS_PERMISSIONS);
        }
    }
}

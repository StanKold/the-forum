package com.example.forum.services;

import com.example.forum.models.Comment;
import com.example.forum.models.CommentsFilterDto;
import com.example.forum.models.User;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getComments();

    Comment getComment(int id);

    List<Comment> getComments(String username, Optional<String> content, Optional<String> sort);

    List<Comment> getCommentsByPostTitle(String title);

    List<Comment> getCommentsByPostId(int postId);

    Comment createComment(Comment comment, User user);

    void deleteComment(int id, User user);

    Comment update(int id, Comment comment, User user);
    long getCount();

    List<Comment>  getFilteredComments(CommentsFilterDto commentsFilterDto);

}

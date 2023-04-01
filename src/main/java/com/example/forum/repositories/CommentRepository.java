package com.example.forum.repositories;

import com.example.forum.models.Comment;
import com.example.forum.models.CommentsFilterDto;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> getComments();

    Comment getComment(int id);

    List<Comment> getComments(String username, Optional<String> content, Optional<String> sort);

    List<Comment> getCommentsByPostTitle(String title);

    List<Comment> getCommentsByPostId(int postId);

    Comment createComment(Comment comment);

    void delete(int id);

    Comment update(int id, Comment comment);

    long getCount();

    List<Comment>  getFilteredComments(CommentsFilterDto commentsFilterDto);

}

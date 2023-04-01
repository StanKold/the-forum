package com.example.forum.helpers;

import com.example.forum.models.Comment;
import com.example.forum.models.CommentDto;
import com.example.forum.models.ReturnCommentDto;
import com.example.forum.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final CommentService commentService;

    @Autowired
    public CommentMapper(CommentService commentService) {
        this.commentService = commentService;
    }

    public Comment fromDto(int id, CommentDto dto) {
        Comment comment = fromDto(dto);
        comment.setReplyId(id);
        Comment repositoryComment = commentService.getComment(id);
        comment.setCreator(repositoryComment.getCreator());
        return comment;
    }

    public Comment fromDto(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }

    public ReturnCommentDto fromCommenet(Comment comment){
        ReturnCommentDto returnCommentDto = new ReturnCommentDto();
        returnCommentDto.setCommentId(comment.getReplyId());
        returnCommentDto.setAuthor(comment.getCreator().getUsername());
        returnCommentDto.setAuthorId(comment.getCreator().getId());
        returnCommentDto.setContent(comment.getContent());
        returnCommentDto.setPostId(comment.getPost().getPost_id());
        returnCommentDto.setPostTitle(comment.getPost().getTitle());
        returnCommentDto.setCreated(comment.getCreatedOn());

        return returnCommentDto;
    }

}

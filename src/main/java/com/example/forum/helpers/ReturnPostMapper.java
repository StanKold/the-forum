package com.example.forum.helpers;

import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.ReturnPostDTO;
import com.example.forum.services.CommentService;
import com.example.forum.services.PostTagService;
import com.example.forum.services.ReactionService;
import com.example.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;


@Component
public class ReturnPostMapper {
    private final UserService userService;
    private final PostTagService postTagService;
    private final CommentService commentService;

    private final ReactionService reactionService;

    @Autowired
    public ReturnPostMapper(UserService userService, PostTagService postTagService, CommentService commentService, ReactionService reactionService) {
        this.userService = userService;
        this.postTagService = postTagService;
        this.commentService = commentService;
        this.reactionService = reactionService;
    }


    public ReturnPostDTO fromPost(Post post) {
        ReturnPostDTO toReturn = new ReturnPostDTO();

        toReturn.setId(post.getPost_id());
        toReturn.setTitle(post.getTitle());
        toReturn.setContent(post.getContent());
        toReturn.setAuthor(userService.getByID(post.getUser_id()).getUsername());
        toReturn.setLikes(post.getLikes());
        toReturn.setDislikes(post.getDislikes());
        toReturn.setApproval(toReturn.getLikes() - toReturn.getDislikes());
        toReturn.setComments(commentService.getCommentsByPostId(post.getPost_id()).stream().map(Comment::toString).collect(Collectors.toList()));
        toReturn.setTagList(String.join(", ",post.getTags().stream().map(tag -> tag.getContent()).collect(Collectors.toList())));
        toReturn.setDays_old((int) ChronoUnit.DAYS.between(post.getCreatedOn(),LocalDate.now()));
        toReturn.setCreator(userService.getByUsername(toReturn.getAuthor()));
        toReturn.setComment(commentService.getCommentsByPostId(post.getPost_id()));
        toReturn.setLiked(reactionService.getLiked(post.getPost_id()));
        toReturn.setDisliked(reactionService.getDisliked(post.getPost_id()));
        toReturn.setCommentsCount(commentService.getCommentsByPostId(post.getPost_id()).size());
        toReturn.setDate(post.getCreatedOn());
        return toReturn;
    }


}

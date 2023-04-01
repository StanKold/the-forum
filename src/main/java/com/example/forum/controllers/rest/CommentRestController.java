package com.example.forum.controllers.rest;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.CommentMapper;
import com.example.forum.models.Comment;
import com.example.forum.models.CommentDto;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.services.CommentService;
import com.example.forum.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/posts")
@Tag(name = "Comment Controller", description = "Operations with comments")
public class CommentRestController {

    private final CommentMapper commentMapper;

    private final CommentService commentService;

    private final AuthenticationHelper authenticationHelper;

    private final PostService service;

    @Autowired
    public CommentRestController(CommentMapper commentMapper, CommentService commentService,
                                 AuthenticationHelper authenticationHelper, PostService service) {
        this.commentMapper = commentMapper;
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
        this.service = service;
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "409", description = "Invalid post")
    @Operation(summary = "Add comment")
    @PostMapping("/{id}/comments")
    public Comment addComment(@RequestHeader String authHeader, @PathVariable int id,
                              @Valid @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            Comment comment = commentMapper.fromDto(commentDto);
            Post post = service.getPost(id);
            comment.setPost(post);
            return commentService.createComment(comment, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Get comments by Post ID")
    @GetMapping(("/{id}/comments"))
    public List<Comment> getComments(@RequestHeader String authHeader, @PathVariable int id) {

        try {
            authenticationHelper.tryGetUser(authHeader);
            service.getPost(id);
            return commentService.getCommentsByPostId(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "409", description = "Invalid Filter")
    @Operation(summary = "Filter comments by username")
    @GetMapping("/comments/filter-by-user")
    public List<Comment> getUserComments(@RequestHeader String authHeader, @RequestParam String username,
                                         @RequestParam(required = false) Optional<String> content,
                                         @RequestParam(required = false) Optional<String> sort) {

        try {
            authenticationHelper.tryGetUser(authHeader);
            return commentService.getComments(username, content, sort);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Get all comments")
    @GetMapping(("/comments"))
    public List<Comment> getAllComments(@RequestHeader String authHeader) {
        try {
            authenticationHelper.tryGetUser(authHeader);
            return commentService.getComments();
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "409", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Delete comment")
    @DeleteMapping(("/{postId}/comments/{commentId}"))
    public void deleteComment(@RequestHeader String authHeader, @PathVariable int postId,
                              @PathVariable int commentId) {
        try {
            service.getPost(postId);
            User user = authenticationHelper.tryGetUser(authHeader);
            commentService.deleteComment(commentId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "409", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Update comment")
    @PutMapping("/{postId}/comments/{commentId}")
    public Comment updateComment(@RequestHeader String authHeader, @PathVariable int postId,
                                 @PathVariable int commentId, @Valid @RequestBody CommentDto dto) {
        try {
            Post post = service.getPost(postId);
            Comment comment = commentMapper.fromDto(commentId, dto);
            comment.setPost(post);
            User user = authenticationHelper.tryGetUser(authHeader);
            return commentService.update(commentId, comment, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

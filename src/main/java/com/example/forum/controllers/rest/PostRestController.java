package com.example.forum.controllers.rest;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.PostMapper;
import com.example.forum.helpers.ReturnPostMapper;
import com.example.forum.models.Post;
import com.example.forum.models.PostDTO;
import com.example.forum.models.ReturnPostDTO;
import com.example.forum.models.User;
import com.example.forum.services.PostService;
import com.example.forum.services.ReactionService;
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


// Postman link in README
// ^^^^^^^^^^^^^^^^^^^^^^

@RestController
@Tag(name = "Post Controller", description = "Operations with posts")
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService service;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;
    private final ReactionService reactionService;

    private final ReturnPostMapper returnPostMapper;


    @Autowired
    public PostRestController(PostService service, AuthenticationHelper authenticationHelper, PostMapper postMapper,
                              ReactionService reactionService, ReturnPostMapper returnPostMapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
        this.reactionService = reactionService;
        this.returnPostMapper = returnPostMapper;
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Get all posts",
            description = "Retrieve a list of all posts")
    public List<Post> getAllPosts(@RequestHeader String authHeader) {
        try {
            authenticationHelper.tryGetUser(authHeader);
            return service.getAllPosts();
        }
        catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Invalid filter")
    @Operation(summary = "Get posts by user",
            description = "Retrieve a list of post by username")
    @GetMapping("/filter-by-user")
    public List<Post> getUserPosts(@RequestHeader String authHeader,
                                   @RequestParam String username,
                                   @RequestParam(required = false) Optional<String> title,
                                   @RequestParam(required = false) Optional<String> content,
                                   @RequestParam(required = false) Optional<String> sort) {

        try {
           User user = authenticationHelper.tryGetUser(authHeader);
            return service.getUserPosts(user.getId(), title, content, sort);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @Operation(summary = "Get post by ID",
            description = "Get a single post")
    @GetMapping("/{id}")
    public ReturnPostDTO get(@RequestHeader String authHeader, @PathVariable int id) {

        try {
            authenticationHelper.tryGetUser(authHeader);
            return returnPostMapper.fromPost(service.getPost(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Invalid post")
    @Operation(summary = "Upload post")
    @PostMapping
    public Post create(@RequestHeader String authHeader, @Valid @RequestBody PostDTO postDTO) {

        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            Post post = postMapper.fromDto(postDTO);
            service.createPost(post, user);
            return post;
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
    @ApiResponse(responseCode = "409", description = "Invalid post")
    @Operation(summary = "Update post")
    @PutMapping("/{id}")
    public Post update(@RequestHeader String authHeader, @PathVariable int id, @Valid @RequestBody PostDTO postDTO) {
        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            Post post = postMapper.fromDto(id, postDTO);
            return service.update(id, post, user);

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
    @ApiResponse(responseCode = "409", description = "Invalid post")
    @Operation(summary = "Delete post")
    @DeleteMapping("/{id}")
    public void delete(@RequestHeader String authHeader, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            service.delete(id, user);
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
    @Operation(summary = "Add reaction to post")
    @PostMapping(("/{id}/add-reaction/{reaction}"))
    public void reactPost(@RequestHeader String authHeader, @PathVariable int id, @PathVariable String reaction) {
        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            reaction = reaction.trim().toLowerCase();
            if (!"like".equals(reaction) && !"dislike".equals(reaction)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("%s is not a valid reaction, please use: like or dislike", reaction));
            }
            reactionService.addReaction(id, reaction, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Search posts by tag and/or title")
    @GetMapping("/search")
    public List<Post> searchPost(@RequestHeader String authHeader, @RequestParam
            (required = false) Optional <String> tag, @RequestParam Optional<String> title) {
      try {
          authenticationHelper.tryGetUser(authHeader);
          return service.searchPost(title,tag);
      }catch (AuthorizationException e) {
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
      }
    }
    @Operation(summary = "Get most recent")
    @GetMapping("/most-recent") //guest access no Authorization
    public List<Post> getRecentPosts() {
        return service.getMostRecentPosts();
    }
    @Operation(summary = "Get most commented")
    @GetMapping(("/most-commented")) //guest access no Authorization
    public List<Post> getTopCommentedPosts() {
        return service.getTopCommentedPosts();
    }
}

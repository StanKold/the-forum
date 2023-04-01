package com.example.forum.controllers.rest;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.TagMapper;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.TagDTO;
import com.example.forum.models.User;
import com.example.forum.services.PostService;
import com.example.forum.services.PostTagService;
import com.example.forum.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/api/posts")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Controller", description = "Operations with tags")
public class TagRestController {

    private final PostTagService postTagService;

    private final TagService tagService;

    private final AuthenticationHelper authenticationHelper;

    private final TagMapper tagMapper;

    private final PostService service;

    @Autowired
    public TagRestController(PostTagService postTagService, TagService tagService, AuthenticationHelper authenticationHelper,
                             TagMapper tagMapper, PostService service) {
        this.postTagService = postTagService;
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
        this.tagMapper = tagMapper;
        this.service = service;
    }

    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Get tags by Post ID")
    @GetMapping("/{id}/tag")
    public List<String> getTag(@RequestHeader String authHeader, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(authHeader);
            return postTagService.getTags(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Add tag to post")
    @PostMapping("/{id}/tag")
    public void addPostTag(@RequestHeader String authHeader, @PathVariable int id, @Valid @RequestBody TagDTO tagDTO) {
        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            Post post = service.getPost(id);
            Tag tag = tagMapper.getTagFromDTO(tagDTO);
            postTagService.addPostTag(user, post, tag);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Remove tag from post")
    @DeleteMapping("/{id}/tag/{tagId}")
    public void removePostTag(@RequestHeader String authHeader, @PathVariable int id, @PathVariable int tagId) {
        try {
            User user = authenticationHelper.tryGetUser(authHeader);
            Post post = service.getPost(id);
            Tag tag = tagService.getTag(tagId);
            postTagService.removePostTag(user, post, tag);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

package com.example.forum;

import com.example.forum.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.HashSet;

public class Helpers {

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setAdmin(false);
        mockUser.setBlocked(false);
        mockUser.setAvatar_url("http://nmssddasd");
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setEmail("mock@user.com");
        mockUser.setCreatedOn(LocalDate.now());
        mockUser.setBlocked(false);
        return mockUser;
    }

    public static Post createMockPost() {
var mockPost= new Post();

        mockPost.setPost_id(1);
        mockPost.setTitle("first mock post");
        mockPost.setContent("first mock post content made for test");
        mockPost.setTags(new HashSet<>());
        mockPost.setComments(new HashSet<>());
        mockPost.setTags(new HashSet<>());
        mockPost.setLikes(0);
        mockPost.setDislikes(0);
        mockPost.setUser_id(1);
        mockPost.setCreatedOn(LocalDate.now());

    return mockPost;
    }


    public static Reaction createMockReaction() {
        var mockReaction= new Reaction();

        mockReaction.setId(1);
        mockReaction.setPost(1);
        mockReaction.setUser(1);
        mockReaction.setReaction("finger");

        return mockReaction;
    }


    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Comment createMockComment(){
        var mockComment = new Comment();

        mockComment.setReplyId(1);
        mockComment.setPost(new Post());
        mockComment.setContent("verry interestin");
        mockComment.setCreator(new User());

        return mockComment;
    }

    public static Phone createMockPhone(){
        Phone mockPhone = new Phone();
        mockPhone.setPhoneId(1);
        mockPhone.setPhoneNumber("phonenumber111111");
        return mockPhone;

    }

    public static Tag createTag () {
        Tag mockTag = new Tag();
        mockTag.setTagID(1);
        mockTag.setContent("new");
        return mockTag;
    }

    public static TagFilterDto createTagFilter(){
        return new TagFilterDto();
    }

}

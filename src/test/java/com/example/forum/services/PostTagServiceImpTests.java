package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.PostTagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.example.forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class PostTagServiceImpTests {

    @Mock
    PostTagRepository mockRepository;


    @InjectMocks
    PostTagServiceImpl mockService;


    @Test
    public void addPostTag_Should_ThrowException_when_UserBlocked() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        mockAuthor.setBlocked(true);
        mockPost.setUser_id(mockAuthor.getId());

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.addPostTag(mockAuthor,mockPost,mockTag));
    }

    @Test
    public void addPostTag_Should_ThrowException_when_UserIsNotAdmin() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockUser.setAdmin(false);
        mockPost.setUser_id(mockAuthor.getId());

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.addPostTag(mockUser,mockPost,mockTag));
    }

    @Test
    public void addPostTag_Should_ThrowException_when_UserIsNotCreator() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockPost.setUser_id(mockAuthor.getId());

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.addPostTag(mockUser,mockPost,mockTag));
    }

    @Test
    public void addPostTag_Should_CallRepository_when_UserIsCreator() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        mockService.addPostTag(mockAuthor,mockPost,mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).addPostTag(mockPost,mockTag);
    }

    @Test
    public void addPostTag_Should_CallRepository_when_UserIsAdmin() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        User mockAdmin = createMockUser();
        mockAdmin.setAdmin(true);
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        mockService.addPostTag(mockAdmin,mockPost,mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).addPostTag(mockPost,mockTag);
    }

    @Test
    public void getAllTags_Should_CallRepository(){
        // Arrange, Act
        Post mockPost = createMockPost();
        mockService.getTags(mockPost.getPost_id());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAllPostTags(mockPost.getPost_id());
    }



    @Test
    public void removePostTag_Should_ThrowException_when_UserBlocked() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        mockAuthor.setBlocked(true);
        mockPost.setUser_id(mockAuthor.getId());

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.removePostTag(mockAuthor,mockPost,mockTag));
    }

    @Test
    public void removePostTag_Should_ThrowException_when_UserIsNotAdmin() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockUser.setAdmin(false);
        mockPost.setUser_id(mockAuthor.getId());

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.removePostTag(mockUser,mockPost,mockTag));
    }

    @Test
    public void removePostTag_Should_ThrowException_when_UserIsNotCreator() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockPost.setUser_id(mockAuthor.getId());

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.removePostTag(mockUser,mockPost,mockTag));
    }

    @Test
    public void removePostTag_Should_CallRepository_when_UserIsCreator() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        mockService.removePostTag(mockAuthor,mockPost,mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).removePostTag(mockPost,mockTag);
    }

    @Test
    public void removePostTag_Should_CallRepository_when_UserIsAdmin() {
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();
        User mockAuthor = createMockUser();
        User mockAdmin = createMockUser();
        mockAdmin.setAdmin(true);
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        mockService.removePostTag(mockAdmin,mockPost,mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).removePostTag(mockPost,mockTag);
    }
}

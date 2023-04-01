package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImpTests {

    @Mock
    PostRepository mockRepository;

    @InjectMocks
    UserServiceImpl userService;

    @InjectMocks
    PostServiceImpl service;

    @Test
    public void getAllPosts_Should_CallRepository() {

        // Arrange,Act
        service.getAllPosts();

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getPosts();
    }


    @Test
    public void getPost_Should_ReturnPost_When_MatchByIdExist() {
        // Arrange
        Post mockPost = createMockPost();


        Mockito.when(mockRepository.getPost(Mockito.anyInt()))
                .thenReturn(mockPost);

        // Act
        Post result = service.getPost(mockPost.getPost_id());

        // Assert
        Assertions.assertEquals(mockPost, result);
    }
    @Test
    public void createPost_Should_CallRepository_when_UserNotBanned() {
        //Arrange
        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        //Act
        service.createPost(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).createPost(mockPost);
    }


    @Test
    public void createPost_Should_Throw_When_PostWithTitleExists() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.existTitle(mockPost.getTitle()))
                .thenReturn(true);

        // Act, Assert
        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> service.createPost(mockPost, mockUser));
    }


    @Test
    public void createPost_Should_ThrowException_when_UserBlocked() {
        //Arrange
        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.createPost(mockPost, mockUser));
    }

    @Test
    public void deletePost_Should_ThrowException_when_UserBlocked() {
        //Arrange
        Post mockPost = createMockPost();
        User mockAuthor = createMockUser();
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockUser.setBlocked(true);
        mockPost.setUser_id(mockAuthor.getId());

        // Act
        Mockito.when(mockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);

        // Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.delete(mockPost.getPost_id(), mockUser));
    }

    @Test
    public void deletePost_Should_ThrowException_when_UserIsNotAdminAndNotCreator() {
        //Arrange
        Post mockPost = createMockPost();
        User mockAuthor = createMockUser();
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockUser.setAdmin(false);
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        Mockito.when(mockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.delete(mockPost.getPost_id(), mockUser));
    }

    @Test
    public void deletePost_CallRepository_when_UserCreator() {
        //Arrange
        User mockAuthor = createMockUser();
        Post mockPost = createMockPost();
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        Mockito.when(mockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);

        service.delete(mockPost.getPost_id(), mockAuthor);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockPost.getPost_id());
    }
    @Test
    public void deletePost_CallRepository_when_UserAdmin() {
        //Arrange
        User mockAuthor = createMockUser();
        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        Mockito.when(mockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);

        service.delete(mockPost.getPost_id(), mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockPost.getPost_id());
    }


    @Test
    public void updatePost_Should_ThrowException_when_UserBlocked() {
        //Arrange
        Post mockPost = createMockPost();
        Post udatePost = createMockPost();
        udatePost.setTitle("updated title for post");
        User mockAuthor = createMockUser();
        mockAuthor.setBlocked(true);
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        Mockito.when(mockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(mockPost.getPost_id(),udatePost,mockAuthor));
    }
    @Test
    public void updatePost_Should_ThrowException_when_UserIsNotCreator() {
      //Arrange
        Post mockPost = createMockPost();
        Post udatePost = createMockPost();
        udatePost.setTitle("updated title for post");
        User mockAuthor = createMockUser();
        User mockUser = createMockUser();

        mockUser.setId(2);
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        Mockito.when(mockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);

        //Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(mockPost.getPost_id(),udatePost,mockUser));
    }


    @Test
    public void updatePost_CallRepository_when_UserCreator() {
        //Arrange
        User mockAuthor = createMockUser();
        Post mockPost = createMockPost();
        Post udatePost = createMockPost();
        udatePost.setTitle("updated title for post");
        mockPost.setUser_id(mockAuthor.getId());

        //Act
        Mockito.when(mockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);

        service.update(mockPost.getPost_id(),udatePost,mockAuthor);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost.getPost_id(),udatePost);
    }

    @Test
   public void getTopCommentedPost_Should_CallRepository() {

        // Arrange,Act
        service.getTopCommentedPosts();

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getTopCommentedPosts();
    }

    @Test
    public void getUserPost(){

        //Act
        service.getUserPosts(1, Optional.of("post"),
                Optional.of("new"),
                Optional.of("title desc"));



        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getUserPosts(1, Optional.of("post"),
                        Optional.of("new"),
                        Optional.of("title desc"));
    }

    @Test
    public void getMostRecent(){
        // Act
        service.getMostRecentPosts();

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getMostRecentPosts();
    }

    @Test
    public void searchPost(){
        //Arrange
        Post mockPost = createMockPost();
        Tag mockTag = createTag();

        //Act
        service.searchPost(Optional.of(mockPost.getTitle()),Optional.of(mockTag.getContent()));

        //Assert
        Mockito.verify(mockRepository,Mockito.times(1)).
                searchPost(Optional.of(mockPost.getTitle()),Optional.of(mockTag.getContent()));
    }
}

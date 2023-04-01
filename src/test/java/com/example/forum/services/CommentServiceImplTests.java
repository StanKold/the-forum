package com.example.forum.services;


import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Comment;
import com.example.forum.models.User;
import com.example.forum.repositories.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.forum.Helpers.createMockComment;
import static com.example.forum.Helpers.createMockUser;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {

    @Mock
    CommentRepository mockRepository;

    @InjectMocks
    CommentServiceImpl service;

    @Test
    public void createComment_Should_ThrowException_when_UserBlocked() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.createComment(mockComment, mockUser));
    }


    @Test
    public void createComment_Should_CallRepository_when_UserNotBanned() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();

        service.createComment(mockComment, mockUser);
        Mockito.verify(mockRepository, Mockito.times(1)).createComment(mockComment);
    }



    @Test
    public void getComment_Should_CallRepository() {
       service.getComment(1);
        Mockito.verify(mockRepository, Mockito.times(1)).getComment(1);
    }
    @Test
    public void getCount_Should_CallRepository() {
        service.getCount();
        Mockito.verify(mockRepository, Mockito.times(1)).getCount();
    }
    @Test
    public void getCommentsByPostTitle_Should_CallRepository() {
        service.getCommentsByPostTitle("test");
        Mockito.verify(mockRepository, Mockito.times(1)).getCommentsByPostTitle("test");;
    }
    @Test
    public void getCommentsByPostId_Should_CallRepository() {
        service.getCommentsByPostId(1);
        Mockito.verify(mockRepository, Mockito.times(1)).getCommentsByPostId(1);;
    }

    @Test
    public void getComments_Should_CallRepository() {
        service.getComments();
        Mockito.verify(mockRepository, Mockito.times(1)).getComments();
    }

    @Test
    public void deleteComment_Should_ThrowException_when_UserBlocked() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        mockComment.setCreator(mockUser);

        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteComment(mockComment.getReplyId(), mockUser));
    }
    @Test
    public void deleteComment_Should_ThrowException_when_UserNotCreator() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteComment(mockComment.getReplyId(), mockUser));
    }

    @Test
    public void deleteComment_CallRepository_when_UserCreator() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockComment.setCreator(mockUser);

        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        service.deleteComment(1, mockUser);
        Mockito.verify(mockRepository, Mockito.times(1)).delete(1);
    }


    @Test
    public void checkModifyPermissions_Should_ThrowException_when_UserBlocked() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockUser.setBlocked(true);


        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteComment(mockComment.getReplyId(), mockUser));
    }

    @Test
    public void checkModifyPermissions_Should_ThrowException_when_UserNotAuthor() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteComment(mockComment.getReplyId(), mockUser));
    }


    @Test
    public void update_CallRepository_when_UserCreator() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockComment.setCreator(mockUser);

        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        service.update(1,mockComment, mockUser);
        Mockito.verify(mockRepository, Mockito.times(1)).update(1,mockComment);
    }


    @Test
    public void update_Should_ThrowException_when_UserBlocked() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockUser.setBlocked(true);


        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(1,mockComment, mockUser));
    }

    @Test
    public void update_Should_ThrowException_when_UserNotAuthor() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.getComment(Mockito.anyInt())).thenReturn(mockComment);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(1,mockComment, mockUser));
    }







}

package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.models.TagFilterDto;
import com.example.forum.models.User;
import com.example.forum.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.forum.Helpers.createMockUser;
import static com.example.forum.Helpers.createTagFilter;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserRepository mockRepository;

    @Mock
    AuthenticationHelper mockAuthentication;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void create_Should_ThrowException_when_UserExists() {

        User mockUser = createMockUser();

        Mockito.when(mockRepository.getByUsername(Mockito.anyString()))
                .thenReturn(mockUser);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> service.create(mockUser));
    }

    @Test
    public void create_Should_ThrowException_when_EmailExists() {

        User mockUser = createMockUser();

        Mockito.when(mockRepository.getByUsername(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        Mockito.when(mockRepository.getUserByEmail(Mockito.anyString()))
                .thenReturn(mockUser);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> service.create(mockUser));
    }


    @Test
    public void create_Should_CallRepo_when_userNew() {

        User mockUser = createMockUser();

        Mockito.when(mockRepository.getByUsername(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        Mockito.when(mockRepository.getUserByEmail(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        service.create(mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).create(mockUser);
    }

    @Test
    public void delete_Should_Throw_when_NotAuthorized() {

        User mockUser = createMockUser();
        mockUser.setId(1);

        User mockUser2 = createMockUser();
        mockUser2.setUsername("newUsername");
        mockUser2.setId(2);
        mockUser2.setEmail("newmail@");

        Mockito.when(mockRepository.getByID(1))
                .thenReturn(mockUser);


        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.delete(1, mockUser2));

    }

    @Test
    public void delete_Should_CallRepository_when_Authorized() {

        User mockUser = createMockUser();
        mockUser.setId(1);

        User mockUser2 = createMockUser();
        mockUser2.setUsername("newUsername");
        mockUser2.setId(2);
        mockUser2.setEmail("newmail@");
        mockUser2.setAdmin(true);

        Mockito.when(mockRepository.getByID(1))
                .thenReturn(mockUser);

        service.delete(1, mockUser2);
        Mockito.verify(mockRepository, Mockito.times(1)).delete(1);

    }




    @Test
    public void update_Should_ThrowException_when_NotAuthorized() {

        User mockUser = createMockUser();
        mockUser.setId(1);

        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(mockUser, mockUser2));

    }

    @Test
    public void update_Should_ThrowException_when_UserNameChanged() {

        User mockUser = createMockUser();
        mockUser.setId(1);

        User mockUser2 = createMockUser();
        mockUser2.setUsername("newUsername");
        mockUser2.setId(1);

        Mockito.when(mockRepository.getByID(1)).thenReturn(mockUser2);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(mockUser, mockUser2));

    }

    @Test
    public void update_Should_ThrowException_when_EmailDuplicate() {

        User mockUser = createMockUser();
        mockUser.setId(1);

        User mockUser2 = createMockUser();
        mockUser2.setId(1);

        User mockUser3 = createMockUser();
        mockUser3.setId(2);
        mockUser3.setUsername("different");

        Mockito.when(mockRepository.getByID(Mockito.anyInt())).thenReturn(mockUser2);


        Mockito.when(mockRepository.getUserByEmail(Mockito.anyString()))
                .thenReturn(mockUser3);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> service.update(mockUser, mockUser2));

    }

    @Test
    public void update_Should_CallRepository_when_EmailNotDuplicate() {

        User mockUser = createMockUser();
        mockUser.setId(1);

        User mockUser2 = createMockUser();
        mockUser2.setId(1);


        Mockito.when(mockRepository.getByID(Mockito.anyInt())).thenReturn(mockUser2);


        Mockito.when(mockRepository.getUserByEmail(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        service.update(mockUser,mockUser2);
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void setAdmin_Should_ThrowException_when_userNotAdmin() {

        User mockUser = createMockUser();
        mockUser.setId(1);

                Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.setAdmin(1, mockUser));

    }
    @Test
    public void setAdmin_Should_ThrowException_when_userNotAuthorized() {

        User mockUser = createMockUser();
        mockUser.setId(1);


        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.setAdmin(1, mockUser));

    }

    @Test
    public void setAdmin_Should_CallRepository_when_userParametersValid() {

        User mockUser = createMockUser();
        mockUser.setId(1);
        mockUser.setAdmin(true);

        User mockUser2 = createMockUser();
        mockUser.setId(2);

        Mockito.when(mockRepository.getByID(1))
                .thenReturn(mockUser2);

        service.setAdmin(1, mockUser);
        Mockito.verify(mockRepository, Mockito.times(1)).setAdmin(mockUser2);

    }

    @Test
    public void blockUser_Should_ThrowException_when_userNotAdmin() {
        User mockUser = createMockUser();
        mockUser.setId(1);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.blockUser(1, mockUser));


    }

    @Test
    public void blockUser_Should_ThrowException_when_SelfBlock() {
        User mockUser = createMockUser();
        mockUser.setId(1);
        mockUser.setAdmin(true);


        Mockito.when(mockRepository.getByID(Mockito.anyInt()))
                .thenReturn(mockUser);

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.blockUser(1, mockUser));
    }

    @Test
    public void blockUser_Should_CallRepository_when_ParametersValid() {
        User mockUser = createMockUser();
        mockUser.setId(1);
        mockUser.setAdmin(true);

        User mockUser2 = createMockUser();
        mockUser.setId(2);

        Mockito.when(mockRepository.getByID(Mockito.anyInt()))
                .thenReturn(mockUser2);

        service.blockUser(1, mockUser);
        Mockito.verify(mockRepository, Mockito.times(1)).blockUser(mockUser2);
    }


    @Test
    public void getByUsername_CallRepository() {
       service.getByUsername("test");
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByUsername("test");
    }



    @Test
    public void getByID_CallRepository() {
        service.getByID(1);
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByID(1);
    }

    @Test
    public void getCount_CallRepository() {
        service.getCount();
        Mockito.verify(mockRepository, Mockito.times(1))
                .getCount();
    }

    @Test
    public void getAll_CallRepository() {
        service.getAll();
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }



}

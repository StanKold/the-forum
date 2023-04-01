package com.example.forum.services;

import com.example.forum.Helpers;
import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Phone;
import com.example.forum.models.User;
import com.example.forum.repositories.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PhoneServiceImplTests {

    @Mock
    PhoneRepository repository;
    @InjectMocks
    PhoneServiceImpl service;

    @Test
    public void addPhone_Should_ThrowException_when_UserNotAdmin() {
        User mockUser = Helpers.createMockUser();
        Phone mockPhone = Helpers.createMockPhone();

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.addPhone(mockPhone, mockUser));
    }

    @Test
    public void addPhone_Should_ThrowException_when_DuplicatePhone() {
        User mockUser = Helpers.createMockUser();
        mockUser.setAdmin(true);
        Phone mockPhone = Helpers.createMockPhone();

        Mockito.when(repository.getPhone(Mockito.anyString())).thenReturn(mockPhone);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> service.addPhone(mockPhone, mockUser));
    }

    @Test
    public void addPhone_Should_CallRepository_when_ParametersValid() {
        User mockUser = Helpers.createMockUser();
        mockUser.setAdmin(true);
        Phone mockPhone = Helpers.createMockPhone();

        Mockito.when(repository.getPhone(Mockito.anyString())).
                thenThrow(EntityNotFoundException.class);

        service.addPhone(mockPhone, mockUser);
        Mockito.verify(repository, Mockito.times(1)).addPhone(mockPhone);
    }

    @Test
    public void updatePhone_Should_ThrowException_when_UserNotAdmin() {
        User mockUser = Helpers.createMockUser();
        Phone mockPhone = Helpers.createMockPhone();

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.addPhone(mockPhone, mockUser));
    }

    @Test
    public void updatePhone_Should_ThrowException_when_DuplicatePhone() {
        User mockUser = Helpers.createMockUser();
        mockUser.setAdmin(true);
        Phone mockPhone = Helpers.createMockPhone();
        Phone mockPhone2 = Helpers.createMockPhone();

        Mockito.when(repository.getPhone(Mockito.anyString())).thenReturn(mockPhone2);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> service.addPhone(mockPhone, mockUser));
    }

    @Test
    public void updatePhone_Should_CallRepository_when_ParametersValid() {
        User mockUser = Helpers.createMockUser();
        mockUser.setAdmin(true);
        Phone mockPhone = Helpers.createMockPhone();

        Mockito.when(repository.getPhone(Mockito.anyString())).
                thenThrow(EntityNotFoundException.class);

        service.updatePhone(mockPhone, mockUser);
        Mockito.verify(repository, Mockito.times(1)).updatePhone(mockPhone);
    }



    @Test
    public void updatePhone_Should_Throw_when_DuplicateExist() {
        User mockUser = Helpers.createMockUser();
        mockUser.setAdmin(true);
        Phone mockPhone = Helpers.createMockPhone();
        mockPhone.setPhoneId(1);
        Phone mockPhone2 = Helpers.createMockPhone();
        mockPhone2.setPhoneId(2);

        Mockito.when(repository.getPhone(Mockito.anyString())).
                thenReturn(mockPhone2);

        Assertions.assertThrows(
               DuplicateEntityException.class,
                () -> service.updatePhone(mockPhone, mockUser));

    }

    @Test
    public void deletePhone_Should_ThrowException_when_UserNotAdmin() {
        User mockUser = Helpers.createMockUser();
        Phone mockPhone = Helpers.createMockPhone();

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deletePhone(mockPhone, mockUser));
    }

    @Test
    public void deletePhone_Should_CallRepository_when_ParametersValid() {
        User mockUser = Helpers.createMockUser();
        mockUser.setAdmin(true);
        Phone mockPhone = Helpers.createMockPhone();


        service.deletePhone(mockPhone, mockUser);
        Mockito.verify(repository, Mockito.times(1)).deletePhone(mockPhone);
    }

    @Test
    public void getPhoneByUser_CallRepository_() {
        service.getPhoneByUser(1);
        Mockito.verify(repository, Mockito.times(1)).getPhoneByUser(1);
    }

    @Test
    public void getPhones_CallRepository_() {
        service.getPhones();
        Mockito.verify(repository, Mockito.times(1)).getAllPhones();
    }

    @Test
    public void getPhone_CallRepository_() {
        service.getPhone(1);
        Mockito.verify(repository, Mockito.times(1)).getPhone(1);
    }
}

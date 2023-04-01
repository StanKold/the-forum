package com.example.forum.services;

import com.example.forum.models.Contact;
import com.example.forum.models.MessageFilterDTO;
import com.example.forum.repositories.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {


    @Mock
    ContactRepository repository;


    @InjectMocks
    ContactServiceImpl service;

    @Test
    public void getMessages_CallRepository_() {
        MessageFilterDTO filter = new MessageFilterDTO();

        service.getMessages (filter);
        Mockito.verify(repository, Mockito.times(1)).getMessages (filter);
    }

    @Test
    public void saveMessage_CallRepository_() {
        Contact contact = new Contact();

        service.saveMessage(contact);
        Mockito.verify(repository, Mockito.times(1)).saveMessage(contact);
    }
    @Test
    public void deleteMessage_CallRepository_() {
        service.deleteMessage(1);
        Mockito.verify(repository, Mockito.times(1)).deleteMessage(1);
    }

}

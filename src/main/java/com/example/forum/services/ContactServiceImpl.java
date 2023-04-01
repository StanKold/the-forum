package com.example.forum.services;

import com.example.forum.models.Contact;
import com.example.forum.models.MessageFilterDTO;
import com.example.forum.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Contact> getMessages(MessageFilterDTO filter) {
        return repository.getMessages(filter);
    }

    @Override
    public void saveMessage(Contact contact) {
repository.saveMessage(contact);
    }

    @Override
    public void deleteMessage(int id) {
repository.deleteMessage(id);
    }
}

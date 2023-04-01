package com.example.forum.repositories;

import com.example.forum.models.Contact;
import com.example.forum.models.MessageFilterDTO;

import java.util.List;

public interface ContactRepository {

    List<Contact> getMessages(MessageFilterDTO messageFilterDTO);

    void saveMessage(Contact contact);

    void deleteMessage(int id);
}


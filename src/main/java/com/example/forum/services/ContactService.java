package com.example.forum.services;

import com.example.forum.models.Contact;
import com.example.forum.models.MessageFilterDTO;

import java.util.List;

public interface ContactService {

    List<Contact> getMessages(MessageFilterDTO messageFilterDTO);

    void saveMessage(Contact contact);

    void deleteMessage(int id);
}

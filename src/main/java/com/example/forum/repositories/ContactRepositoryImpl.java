package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Contact;
import com.example.forum.models.MessageFilterDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ContactRepositoryImpl implements ContactRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public ContactRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Contact> getMessages(MessageFilterDTO messageFilterDTO) {
        try (Session session = sessionFactory.openSession()) {
            Query<Contact> query = session.createQuery("from Contact", Contact.class);
            return query.list();
        }
    }

    @Override
    public void saveMessage(Contact contact) {
        contact.setDate(LocalDate.now());
        System.out.println(contact.getDate());
    try(Session session = sessionFactory.openSession()){
     session.beginTransaction();
     session.persist(contact);
     session.getTransaction().commit();
     }
    }

    @Override
    public void deleteMessage(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getMessage(id));
            session.getTransaction().commit();
        }
    }


    public Contact getMessage(int id) {
        try (Session session = sessionFactory.openSession()) {
            Contact result = session.get(Contact.class, id);
            if (result == null) {
                throw new EntityNotFoundException("Message", "id", String.valueOf(id));
            }
            return result;
        }
    }
}

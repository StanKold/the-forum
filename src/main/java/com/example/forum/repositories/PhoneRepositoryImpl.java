package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Phone;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneRepositoryImpl implements PhoneRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PhoneRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Phone addPhone(Phone phone) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(phone);
            session.getTransaction().commit();
        }
        return phone;
    }

    @Override
    public Phone updatePhone(Phone phone) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(phone);
            session.getTransaction().commit();
        }
        return phone;
    }

    @Override
    public Phone getPhone(int phoneId) {
        try (Session session = sessionFactory.openSession()) {
            Phone phone = session.get(Phone.class, phoneId);

            return phone;
        }

    }

    @Override
    public Phone getPhone(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<Phone> query = session.createQuery("from Phone where " +
                    "phoneNumber = : phoneNumber", Phone.class);

            query.setParameter("phoneNumber", phoneNumber);

            List<Phone> phones = query.list();

            if (phones.isEmpty()) {
                throw new EntityNotFoundException(String.format(String.format("Phone number %s doesn't exist", phoneNumber)));
            }

            return phones.get(0);
        }
    }

    @Override
    public Phone getPhoneByUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Phone> query = session.createQuery("from Phone where " +
                    "user.id = : user_id", Phone.class);

            query.setParameter("user_id", userId);

            List<Phone> phones = query.list();

            if (phones.isEmpty()) {
             return null;
            }

            return phones.get(0);
        }
    }

    @Override
    public void deletePhone(Phone phoneToDelete) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(phoneToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Phone> getAllPhones() {
        try (Session session = sessionFactory.openSession()) {
            Query<Phone> query = session.createQuery("from Phone ", Phone.class);
            List<Phone> phones = query.list();

            if (phones.isEmpty())
                throw new EntityNotFoundException("There are no users with phones");
            return phones;
        }
    }
}

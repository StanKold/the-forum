package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.FilterOptions;
import com.example.forum.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll(FilterOptions filterOptions) {
        List<String> filters = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        filterOptions.getFirstName().ifPresent(value -> {
            filters.add("firstName like :name");
            params.put("name", String.format("%%%s%%", value));
        });

        filterOptions.getUsername().ifPresent(value -> {
            filters.add("username like :username");
            params.put("username", String.format("%%%s%%", value));
        });

        filterOptions.getEmail().ifPresent(value -> {
            filters.add("email like :email");
            params.put("email", String.format("%%%s%%", value));
        });

        StringBuilder queryString = new StringBuilder("from User");
        if (!filters.isEmpty()) {
            queryString
                    .append(" where ")
                    .append(String.join(" and ", filters));
        }
        queryString.append(generateStringFromSort(filterOptions));



        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateStringFromSort(FilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = switch (filterOptions.getSortBy().get()) {
            case "Name" -> "firstName";
            case "Username" -> "username";
            case "Email" -> "email";
            default -> "";
        };

        if (!orderBy.isEmpty()) {
            orderBy = String.format(" order by %s", orderBy);

            if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
                orderBy = String.format("%s desc", orderBy);
            }
        }

        return orderBy;
    }


    @Override
    public User getByUsername(String userName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User " +
                    "where username = :username", User.class);
            query.setParameter("username", userName);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "username", userName);
            }

            return result.get(0);
        }

    }

    @Override
    public User getByID(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", "id", String.valueOf(id));
            }
            return user;
        }
    }

    @Override
    public User create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
        return user;
    }

    @Override
    public void delete(int id) {
        User userToDelete = getByID(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(userToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User " +
                    "where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }

            return result.get(0);
        }
    }

    @Override
    public User setAdmin(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("UPDATE User u SET u.isAdmin = :isAdmin WHERE u.id = :userId");
            query.setParameter("isAdmin", !user.isAdmin());
            query.setParameter("userId", user.getId());
            query.executeUpdate();
            session.getTransaction().commit();
        }
        return getByID(user.getId());
    }

    @Override
    public User blockUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("UPDATE User u SET u.isBlocked = : isBlocked WHERE u.id = :userId");
            query.setParameter("isBlocked", !user.isBlocked());
            query.setParameter("userId", user.getId());
            query.executeUpdate();
            session.getTransaction().commit();
        }
        return getByID(user.getId());
    }

    @Override
    public long getCount() {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("select count (*) from User");
            return (long)query.uniqueResult();
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User ", User.class);
            List<User> users = query.list();
            return users;
        }
    }

}

package com.example.forum.services;

import com.example.forum.models.FilterOptions;
import com.example.forum.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll(FilterOptions filterOptions);

    User getByUsername(String userName);

    User getByID(int id);

    User create(User user);

    void delete(int id, User user);

    User update(User userToUpdate, User loggedInUser);

    User setAdmin(int id, User loggedInUser);

    User blockUser(int id, User loggedInUser);

    long getCount();

    List<User> getAll();
}

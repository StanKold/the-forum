package com.example.forum.repositories;


import com.example.forum.models.FilterOptions;
import com.example.forum.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll(FilterOptions filterOptions);

    User getByUsername(String userName);

    User getByID(int id);

    User create(User user);

    void delete(int id);

    User update(User user);

    User getUserByEmail(String email);

    User setAdmin(User user);

    User blockUser(User user);
    long getCount();

    List<User> getAll();

}

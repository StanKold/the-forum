package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.FilterOptions;
import com.example.forum.models.User;
import com.example.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String ADMIN_RIGHTS = "Only admins can create or remove admin roles";
    public static final String BLOCK_OR_UNBLOCK_OTHER_USERS = "Only admins can block or unblock other users";
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll(FilterOptions filterOptions) {
        return repository.getAll(filterOptions);
    }

    @Override
    public User getByUsername(String userName) {
        return repository.getByUsername(userName);
    }

    @Override
    public User getByID(int id) {
        return repository.getByID(id);
    }

    @Override
    public User create(User user) {
        boolean usernameExists = true;
        boolean emailExists = true;

        try {
            repository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            usernameExists = false;
        }
        if (usernameExists) throw new DuplicateEntityException("User", "username", user.getUsername());

        try {
            repository.getUserByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            emailExists = false;
        }
        if (emailExists) throw new DuplicateEntityException("User", "email", user.getEmail());

        return repository.create(user);
    }

    @Override
    public void delete(int id, User user) {
        User userToDelete = repository.getByID(id);
        if (!(user.isAdmin() || userToDelete.equals(user)))
            throw new AuthorizationException("Only admins or the user itself can delete users");

        repository.delete(id);
    }

    @Override
    public User update(User updatedUser, User loggedInUser) {
        if (!(loggedInUser.isAdmin() || updatedUser.equals(loggedInUser)))
            throw new AuthorizationException("Only admins or the user itself can update user profiles");

        User repositoryUser = repository.getByID(updatedUser.getId());

        if (!updatedUser.getUsername().equals(repositoryUser.getUsername()))
            throw new AuthorizationException("Users can't change their username once created");

        boolean duplicateExists = true;

        try {
            User existingUser = repository.getUserByEmail(updatedUser.getEmail());
            if (existingUser.getId() == updatedUser.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", updatedUser.getEmail());
        }

        return repository.update(updatedUser);

    }

    @Override
    public User setAdmin(int id, User loggedInUser) {
        if (!(loggedInUser.isAdmin()))
            throw new AuthorizationException(ADMIN_RIGHTS);

        User userToMakeAdmin = repository.getByID(id);
        return repository.setAdmin(userToMakeAdmin);
    }

    @Override
    public User blockUser(int id, User loggedInUser) {
        if (!(loggedInUser.isAdmin()))
            throw new AuthorizationException(BLOCK_OR_UNBLOCK_OTHER_USERS);

        User userToBlock = repository.getByID(id);

        if (loggedInUser.equals(userToBlock))
            throw new AuthorizationException("Admins can't block themselves.");

        return repository.blockUser(userToBlock);
    }

    @Override
    public long getCount() {
        return repository.getCount();
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }
}
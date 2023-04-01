package com.example.forum.helpers;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.User;
import com.example.forum.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    public static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password";
    public static final String INVALID_PASSWORD = "Invalid password";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.getByUsername(username);

            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    public User tryGetUser(String headers) {

        try {
            String username = getUsername(headers);
            String password = getPassword(headers);
            User user = userService.getByUsername(username);

            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpace + 1);
    }
    public User verifyAuthentication(String username, String password){
        try{
            User user = userService.getByUsername(username);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new AuthorizationException(WRONG_USERNAME_OR_PASSWORD);
            }
            return user;
        }catch(EntityNotFoundException e){
            throw new AuthorizationException(WRONG_USERNAME_OR_PASSWORD);
        }
    }
    public User verifyPassword(String username, String password){
        try{
            User user = userService.getByUsername(username);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new AuthorizationException(INVALID_PASSWORD);
            }
            return user;
        }catch(EntityNotFoundException e){
            throw new AuthorizationException(INVALID_PASSWORD);
        }
    }

    public User tryGetCurrentUser(HttpSession session) {
        String currentUsername = (String) session.getAttribute("loggedUser");

        if (currentUsername == null) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userService.getByUsername(currentUsername);
    }


}

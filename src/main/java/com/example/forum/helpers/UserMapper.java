package com.example.forum.helpers;

import com.example.forum.models.RegisterDto;
import com.example.forum.models.User;
import com.example.forum.models.UserDto;
import com.example.forum.services.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class UserMapper {

    private final UserService service;

    public UserMapper(UserService service) {
        this.service = service;
    }

    public User fromDto(int id, UserDto dto) {
        User exsistingUser = service.getByID(id);
        dto.setAdmin(exsistingUser.isAdmin());

        if (!(exsistingUser.getAvatar_url().equals("none")) && dto.getAvatar_url() == null) {
            dto.setAvatar_url(exsistingUser.getAvatar_url());
        }

        User user = fromDto(dto);
        user.setId(id);
        return user;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        AddCommonInfo(user,dto.getFirstName(), dto.getLastName(),
                dto.getUsername(), dto.getPassword(), dto.getEmail());

        user.setAdmin(dto.isAdmin());

        user.setAvatar_url(Objects.requireNonNullElse(dto.getAvatar_url(), "none"));
        return user;

    }

    public User createUserFromDto(RegisterDto dto){
        User user = new User();
        AddCommonInfo(user,dto.getFirstName(), dto.getLastName(),
                dto.getUsername(), dto.getPassword(), dto.getEmail());

        user.setAvatar_url(Objects.requireNonNullElse(dto.getAvatar_url(), "none"));
        return user;
    }

    private void AddCommonInfo(User user,String firstName, String lastName, String username, String password, String email) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setEmail(email);

        user.setCreatedOn(LocalDate.now());
    }

    public UserDto toDto(User user){
        UserDto dto = new UserDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAvatar_url(Objects.requireNonNullElse(user.getAvatar_url(), "none"));
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        System.out.println(dto.getPassword());
        dto.setEmail(user.getEmail());
        dto.setCreatedOn(user.getCreatedOn());
        dto.setAdmin(user.isAdmin());
        return dto;
    }

}


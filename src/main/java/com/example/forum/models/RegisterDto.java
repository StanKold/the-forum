package com.example.forum.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 characters long")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 characters long")
    private String lastName;

    @NotBlank(message = "е-mail cannot be empty")
    @Size(min = 10, max = 60, message = "e-mail should be between 10 and 60 characters long")
    private String email;

    @NotBlank(message = "username cannot be empty")
    @Size(min = 5, max = 30, message = "username should be between 5 and 30 characters long")
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 3, max = 30, message = "Password should be between 3 and 30 characters long")
    private String password;

    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirm;

    private String avatar_url;

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}

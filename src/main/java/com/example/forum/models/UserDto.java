package com.example.forum.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserDto {
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 characters long")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 characters long")
    private String lastName;

    @NotBlank(message = "еmail cannot be empty")
    @Size(min = 10, max = 60, message = "email should be between 10 and 60 characters long")
    private String email;

    @NotBlank(message = "username cannot be empty")
    @Size(min = 5, max = 30, message = "username should be between 5 and 30 characters long")
    private String username;

    @NotBlank(message = "password cannot be empty")
    private String password;

    private boolean isAdmin;

    private boolean isBlocked;

    private String phone;
    private String avatar_url;

    private LocalDate createdOn;

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

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }
}

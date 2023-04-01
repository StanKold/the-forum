package com.example.forum.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDto {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 30, message = "Username should be between 5 and 30 characters long")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 3, max = 30, message = "Password should be between 3 and 30 characters long")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

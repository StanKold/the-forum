package com.example.forum.models;



import java.util.Optional;

public class FilterOptions {

    private Optional<String> firstName;
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public FilterOptions() {
        this(null, null, null, null, null);
    }


    public FilterOptions(String firstName, String username, String email,
                         String sortBy, String sortOrder) {
        this.firstName = Optional.ofNullable(firstName);
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }


    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}

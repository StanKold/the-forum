package com.example.forum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private int contact_id;

    @NotBlank(message = "message cannot be empty")
    @Size(min = 5, max = 200, message = "message must be between 5 and 200 characters")
    @Column(name = "contact_text")
    private String contact_text;

    @NotBlank(message = "e-mail cannot be empty")
    @Size(min = 5, max = 50, message = "e-mail must be between 5 and 50 characters")
    @Column(name = "contact_email")
    private String contact_email;

    @NotBlank(message = "name cannot be empty")
    @Size(min = 5, max = 50, message = "name must be between 5 and 50 characters")
    @Column(name = "contact_name")
    private String contact_name;

    @Column(name = "contact_date")
    private LocalDate date;

    public Contact() {
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_text() {
        return contact_text;
    }

    public void setContact_text(String contact_text) {
        this.contact_text = contact_text;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

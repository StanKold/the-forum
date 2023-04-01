package com.example.forum.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class PostDTO {
    @NotBlank(message = "Post title cannot be empty")
    @Size(min = 5, max = 20, message = "Post title must be between 5 and 20 characters")
    private String title;

    @NotBlank(message = "content cannot be empty")
    @Size(min = 20, max = 200, message = "Post content must be between 20 and 200 characters")
    private String content;


    public PostDTO() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

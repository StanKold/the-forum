package com.example.forum.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class CommentDto {


    @NotBlank(message = "Comment shouldn't be empty")
    @Size(min = 5, max = 500, message = "Comment should be between 5 and 500 characters long")
    private String content;

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }
}

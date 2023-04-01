package com.example.forum.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagDTO {
    @NotBlank(message = "Tag can't be empty")
    @Size(min = 3, max = 20, message = "Tag should be between 3 and 20 characters long")
    private String tag;

    public TagDTO() {

    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag.toLowerCase();
    }
}

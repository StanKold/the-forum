package com.example.forum.services;

import com.example.forum.models.Tag;
import com.example.forum.models.TagFilterDto;

import java.util.List;

public interface TagService {
    Tag getTagByName(String name);

    Tag getTag(int tagID);

    List<Tag> getTags(TagFilterDto tagFilterDto);

    void deleteTag(int id);
}

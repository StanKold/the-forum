package com.example.forum.repositories;


import com.example.forum.models.Tag;
import com.example.forum.models.TagFilterDto;

import java.util.List;

public interface TagRepository {

     Tag getTagByName(String name);


    Tag addTag(String name);

    Tag getTag(int tagId);

    List<Tag> getTags(TagFilterDto tagFilterDto);

    void deleteTag(int id);
}
package com.example.forum.helpers;

import com.example.forum.models.Tag;
import com.example.forum.models.TagDTO;
import com.example.forum.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    private final TagService service;

    @Autowired
    public TagMapper(TagService service) {
        this.service = service;
    }

    public Tag getTagFromDTO(TagDTO tagDTO) {
         return service.getTagByName(tagDTO.getTag());

    }

}

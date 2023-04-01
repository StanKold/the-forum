package com.example.forum.services;

import com.example.forum.models.Tag;
import com.example.forum.models.TagFilterDto;
import com.example.forum.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements  TagService{

    private final TagRepository repo;
@Autowired
    public TagServiceImpl(TagRepository repository) {
        this.repo = repository;
    }

    @Override
    public Tag getTagByName(String name) {
        return repo.getTagByName(name);
    }

    @Override
    public Tag getTag(int tagID) {
        return repo.getTag(tagID);
    }

    @Override
    public List<Tag> getTags(TagFilterDto filter) { return repo.getTags(filter); }

    @Override
    public void deleteTag(int id) { repo.deleteTag(id); }
}

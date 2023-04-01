package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTagServiceImpl implements PostTagService {

    private final PostTagRepository repository;
@Autowired
    public PostTagServiceImpl(PostTagRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addPostTag(User user, Post post, Tag tag) {
        if(!(post.getUser_id() == user.getId() || user.isAdmin()) || user.isBlocked()){
            throw new AuthorizationException("Only not blocked author or admin can tag a post!");
        }
        List<String> tags = repository.getAllPostTags(post.getPost_id());
        if(!tags.contains(tag.getContent())){
            repository.addPostTag(post,tag);
        }else{
        repository.removePostTag(post,tag);
    }
}

    @Override
    public List<String> getTags(int id) {
        return repository.getAllPostTags(id);
    }




    @Override
    public void removePostTag(User user, Post post, Tag tag) {
        if(!(post.getUser_id() == user.getId() || user.isAdmin()) || user.isBlocked()){
            throw new AuthorizationException("Only not blocked author or admin can remove tag of a post!");
        }
        repository.removePostTag(post,tag);
    }

}

package com.example.forum.helpers;

import com.example.forum.models.Post;
import com.example.forum.models.PostDTO;
import com.example.forum.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {


    private final PostService postService;

    @Autowired
    public PostMapper(PostService postService) {
        this.postService = postService;
    }

    public Post fromDto(int id, PostDTO postDTO) {
        Post post = fromDto(postDTO);
        post.setPost_id(id);
        Post temp = postService.getPost(id);
        post.setUser_id(temp.getUser_id());
        return post;
    }


    public Post fromDto(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        return post;
    }

    public PostDTO fromPost(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        return postDTO;
    }

    public boolean checkTitleDuplicate(String title){
        return postService.checkTitleDuplicate(title);
    }



}

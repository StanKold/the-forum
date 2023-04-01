package com.example.forum.controllers.mvc;


import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.*;
import com.example.forum.models.*;
import com.example.forum.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostMvcController {
    private final PostService postService;
    private final ReturnPostMapper returnPostMapper;

    private final UserService userService;
    private final PostMapper postMapper;

    private final AuthenticationHelper helper;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final PostTagService postTagService;
    private final TagMapper tagMapper;
    private final ReactionService reactionService;
    private final TagService tagService;


    @Autowired
    public PostMvcController(PostService postService,
                             ReturnPostMapper returnPostMapper,
                             UserService userService,
                             PostMapper postMapper,
                             AuthenticationHelper helper, CommentMapper commentMapper, CommentService commentService, PostTagService postTagService, TagMapper tagMapper, ReactionService reactionService, TagService tagService) {
        this.postService = postService;
        this.returnPostMapper = returnPostMapper;
        this.userService = userService;
        this.postMapper = postMapper;
        this.helper = helper;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
        this.postTagService = postTagService;
        this.tagMapper = tagMapper;
        this.reactionService = reactionService;
        this.tagService = tagService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        if(session.getAttribute("isAdmin") == null){
            return false;
        }
        return (boolean)session.getAttribute("isAdmin");
    }

    @ModelAttribute("userId")
    public int populateUserID(HttpSession session) {
        if(session.getAttribute("userID") == null){
            return -1;
        }
        return (int)session.getAttribute("userID");
    }

    @GetMapping()
    public String showAllPosts(@ModelAttribute("postFilterOptions") PostFilterDto postFilterDto,Model model,HttpSession session) {

        if(session.getAttribute("loggedUser")==null){
           return"redirect:/auth/accessdenied";
        }
        if(postFilterDto.getAuthor()!=null && !postFilterDto.getAuthor().isEmpty()){
            try{
               postFilterDto.setAuthorID(userService.getByUsername(postFilterDto.getAuthor()).getId());

            }catch (EntityNotFoundException e){
                postFilterDto.setAuthorID(-1);
            }
        }

        List<ReturnPostDTO> posts = postService.getAllPosts(postFilterDto).stream()
                                                                .map(returnPostMapper::fromPost)
                                                                .collect(Collectors.toList());

        model.addAttribute("posts",posts);
        model.addAttribute("postFilterOptions",postFilterDto);
        return "PostsView";

    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model,HttpSession session) {
        boolean isAuthor;
        User user=null;
                                 try { if (session.getAttribute("loggedUser")!=null){
                                     user = helper.tryGetCurrentUser(session);
                                     Post post = postService.getPost(id);
                                     isAuthor = (post.getUser_id() == user.getId());
                                 }else {
                                     isAuthor = false;}
                                 }catch (EntityNotFoundException e){
                                     isAuthor=false;
                                 }
        try {
            ReturnPostDTO postDto = returnPostMapper.fromPost(postService.getPost(id));
            model.addAttribute("isAuthor",isAuthor);
            model.addAttribute("commentDTO", new CommentDto());
            model.addAttribute("post", postDto);
            model.addAttribute("author", userService.getByUsername(postDto.getAuthor()));



            if (postDto.getLiked()==null || user == null) {
                model.addAttribute("isLiked", false);
            }else {
                model.addAttribute("isLiked", postDto.getLiked().contains(user.getId()));
            }

            if (postDto.getDisliked()==null || user == null) {
                model.addAttribute("isDisliked",false);
            }else{
                model.addAttribute("isDisliked", postDto.getDisliked().contains(user.getId()));
            }

            return "PostView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }


       @PostMapping("/{id}/comment")
    public String addPostComment(@PathVariable int id,
                                 Model model,
                                 @Valid @ModelAttribute("commentDTO") CommentDto commentDTO,
                                 BindingResult bindingResult,
                                 HttpSession session) {



           boolean isAuthor=false;
           User user=null;
           Post post=null;
           try { user = helper.tryGetCurrentUser(session);
                post = postService.getPost(id);
           }catch (EntityNotFoundException e){
               return "redirect:/auth/accessdenied";
           }
           isAuthor = (post.getUser_id() == user.getId());

           ReturnPostDTO postDto = returnPostMapper.fromPost(postService.getPost(id));
           model.addAttribute("isAuthor",isAuthor);
           model.addAttribute("commentDTO", commentDTO);
           model.addAttribute("post", postDto);
           model.addAttribute("author", userService.getByUsername(postDto.getAuthor()));


           if(bindingResult.hasErrors()){
               return"PostView";
           }

           if(commentDTO.getContent()==null || commentDTO.getContent().isEmpty()){
                bindingResult.rejectValue("content", "content_error", "content cannot be empty");
                  return"PostView";
           }


           Comment comment = commentMapper.fromDto(commentDTO);
           comment.setPost(post);
           commentService.createComment(comment, user);
           return"redirect:/posts/"+id;

    }

    @GetMapping("/new")
    public String showSinglePost(Model model,HttpSession session){
                    try{
                       helper.tryGetCurrentUser(session);
                   }catch(EntityNotFoundException e){
                       return"redirect:/auth/accessdenied";
                   }
        model.addAttribute("postDto", new PostDTO());
        return "NewPostView";
    }


    @PostMapping("/new")
    public String showSinglePost(@Valid @ModelAttribute("postDto") PostDTO postDTO,
                                 BindingResult bindingResult,
                                 HttpSession session) {


        try{ helper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
                  return"redirect:/auth/accessdenied";
        }

       if(bindingResult.hasErrors()){
           return"NewPostView";
       }

        if(postMapper.checkTitleDuplicate(postDTO.getTitle())){
            bindingResult.rejectValue("title", "title_duplicate", "title already exists");
            return"NewPostView";
        }

            Post post = postMapper.fromDto(postDTO);
            User user = userService.getByUsername((String) session.getAttribute("loggedUser"));
            postService.createPost(post, user);
        return "redirect:/posts/"+post.getPost_id();
    }




    @GetMapping("/{id}/edit")
    public String showPostToEdit(@PathVariable int id,Model model,HttpSession session){
       Post post;

       try{
          User user = helper.tryGetCurrentUser(session);
           post = postService.getPost(id);
           model.addAttribute("postId",post.getPost_id());
           model.addAttribute("postDTO", postMapper.fromPost(post));
           model.addAttribute("tags",postService.getPost(id).getTags().stream().map(Tag::toString).collect(Collectors.toList()));
           model.addAttribute("TagDto", new TagDTO());

          if(user.getId()!=post.getUser_id()){
              return"redirect:/auth/accessdenied";
             }
          }catch(EntityNotFoundException e){
              return"redirect:/auth/accessdenied";
          }

        return "EditPostView";
    }


    @PostMapping("/{id}/edit")
    public String showPostEdit(@PathVariable int id,Model model,
            @Valid @ModelAttribute("postDTO") PostDTO postDTO,
                                 BindingResult bindingResult,
                                 HttpSession session) {

                  try{
                      Post post = postService.getPost(id);
                      model.addAttribute("postDTO", postMapper.fromPost(post));
                      helper.tryGetCurrentUser(session);
                      model.addAttribute("postId",id);
                      model.addAttribute("TagDto", new TagDTO());
                  }catch(EntityNotFoundException e){
                      return"redirect:/auth/accessdenied";
                  }


        if(bindingResult.hasErrors()){
            return "EditPostView";
        }

        Post toUpdate = postService.getPost(id);

       List<Post> updated = postService.searchPost(Optional.of(postDTO.getTitle()), Optional.of(postDTO.getTitle()));
       if(!updated.isEmpty() && toUpdate.getPost_id()!=updated.get(0).getPost_id()){
           bindingResult.rejectValue("title", "title_duplicate", "title already exists");
           return "EditPostView";
       }

       User user = userService.getByUsername((String) session.getAttribute("loggedUser"));

       if(user.getId()!=toUpdate.getUser_id()){
           return"redirect:/auth/accessdenied";
       }

        toUpdate.setTitle(postDTO.getTitle());
        toUpdate.setContent(postDTO.getContent());

        postService.update(id, toUpdate, user);
        return String.format("redirect:/posts/%d/edit",toUpdate.getPost_id());
    }


    @PostMapping("/{id}/tags")
    public String showPostEdit(@PathVariable int id,
                               @Valid @ModelAttribute("TagDto") TagDTO tagDTO,
                               BindingResult bindingResult,
                               HttpSession session) {

        try{
            helper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
            return"redirect:/auth/accessdenied";
        }

        if(bindingResult.hasErrors()){
            return "EditPostView";
        }
       Tag tag = tagMapper.getTagFromDTO(tagDTO);
        Post post = postService.getPost(id);
        User user = userService.getByUsername((String) session.getAttribute("loggedUser"));

        if(user.getId()!=post.getUser_id()){
            return"redirect:/auth/accessdenied";
        }

        postTagService.addPostTag(user, post,tag);
        return String.format("redirect:/posts/%d/edit",post.getPost_id());

    }

    @GetMapping("/{id}/like")
    public String likePost(@PathVariable int id,
                               HttpSession session) {
User user;
        try{
          user = helper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
            return"redirect:/auth/accessdenied";
        }


        reactionService.addReaction(id, "like", user);
        return String.format("redirect:/posts/%d",id);

    }

    @GetMapping("/{id}/dislike")
    public String dislikePost(@PathVariable int id,
                               HttpSession session) {
        User user;
        try{
            user = helper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
            return"redirect:/auth/accessdenied";
        }


        reactionService.addReaction(id, "dislike", user);
        return String.format("redirect:/posts/%d",id);

    }


    @GetMapping("/{id}/remove")
    public String removePost(@PathVariable int id,
                                     HttpSession session) {
        if(session.getAttribute("isAdmin")==null || !(boolean)session.getAttribute("isAdmin")){
            return"redirect:/auth/accessdenied";
        }

        User user;
        try{
           user = helper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
            return"redirect:/auth/accessdenied";
        }
        postService.delete(id,user);
        return "redirect:/admin-portal";
    }

    @GetMapping("/comments/{id}/delete")
    public String removeComment(@PathVariable int id,
                             HttpSession session) {

        User user;
        try{
            user = helper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
            return"redirect:/auth/accessdenied";
        }

        if(!user.isAdmin()){
            return"redirect:/auth/accessdenied";
        }

        commentService.deleteComment(id,user);
        return "redirect:/admin-portal";
    }




    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable int id,
                                HttpSession session) {

        User user;
        try{
            user = helper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
            return"redirect:/auth/accessdenied";
        }

        if(!user.isAdmin()){
            return"redirect:/auth/accessdenied";
        }

        tagService.deleteTag(id);
        return "redirect:/admin-portal";
    }


}
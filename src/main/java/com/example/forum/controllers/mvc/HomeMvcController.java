package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.CommentMapper;
import com.example.forum.helpers.ReturnPostMapper;
import com.example.forum.models.*;
import com.example.forum.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final PostService postService;
    private final ReturnPostMapper returnPostMapper;
    private final UserService userService;
    private final CommentService commentService;
    private final AuthenticationHelper helper;
    private final ContactService contactService;
    private final CommentMapper commentMapper;
    private final TagService tagService;

    @Autowired
    public HomeMvcController(PostService postService, ReturnPostMapper returnPostMapper, UserService userService,
                             CommentService commentService, AuthenticationHelper helper, ContactService contactService, CommentMapper commentMapper, TagService tagService) {
        this.postService = postService;
        this.returnPostMapper = returnPostMapper;
        this.userService = userService;
        this.commentService = commentService;
        this.helper = helper;
        this.contactService = contactService;
        this.commentMapper = commentMapper;
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

    @ModelAttribute("currentUserId")
    public int populateCurrentUserId(HttpSession session){

        if(session.getAttribute("userID") == null)
            return -1;

        return (int) session.getAttribute("userID");
    }

    @ModelAttribute("userId")
    public int populateUserID(HttpSession session) {
        if(session.getAttribute("userID") == null){
            return -1;
        }
        return (int)session.getAttribute("userID");
    }





    @ModelAttribute("usersCount")
    public int populateUserCount() {
        return (int)userService.getCount();
    }

    @ModelAttribute("postsCount")
    public int populatePostsCount() {
        return (int)postService.getCount();
    }

    @ModelAttribute("commentsCount")
    public int populateCommentsCount() {
        return (int)commentService.getCount();
    }




    @GetMapping
    public String showHomePage() {
        return "HomeView";
    }

    @ModelAttribute("mostRecent")
    public List<ReturnPostDTO> mostRecent() {
        return postService.getMostRecentPosts().stream().map(returnPostMapper::fromPost).collect(Collectors.toList());
    }

    @ModelAttribute("mostCommented")
    public List<ReturnPostDTO> mostCommented() {
        return postService.getTopCommentedPosts().stream().map(returnPostMapper::fromPost).collect(Collectors.toList());
    }


    @GetMapping("/admin-portal")
    public String handlePortal(Model model, HttpSession session,
                               @ModelAttribute("filterUsers") FilterDto filterDto,
                               @ModelAttribute("filterMessage") MessageFilterDTO messageFilterDTO,
                               @ModelAttribute("filterPosts") PostFilterDto postFilterDto,
                               @ModelAttribute("filterComments") CommentsFilterDto commentsFilterDto,
                               @ModelAttribute("filterTags") TagFilterDto tagFilterDto){
        try{
            helper.tryGetCurrentUser(session);
            if(! (boolean)session.getAttribute("isAdmin")){
                throw new AuthorizationException("Only admins are allowed");
            }

            FilterOptions filterOptions = new FilterOptions(
                    filterDto.getFirstName(),
                    filterDto.getUsername(),
                    filterDto.getEmail(),
                    filterDto.getSortBy(),
                    filterDto.getSortOrder()
            );


            if(commentsFilterDto.getAuthor()!=null && !commentsFilterDto.getAuthor().isEmpty()){
                try{
                commentsFilterDto.setAuthorId(userService.getByUsername(commentsFilterDto.getAuthor()).getId());
                }catch(EntityNotFoundException e){
                    commentsFilterDto.setAuthorId(-1);
                }

            }


            List<User> users = userService.getAll(filterOptions);
            List<Contact>  messages = contactService.getMessages(messageFilterDTO);
            List<ReturnPostDTO> posts = postService.getAllPosts(postFilterDto).stream().map(returnPostMapper::fromPost).collect(Collectors.toList());
            List<ReturnCommentDto>comments=commentService.getFilteredComments(commentsFilterDto).stream().map(commentMapper::fromCommenet).collect(Collectors.toList());
            List<Tag>tags=tagService.getTags(tagFilterDto);
            model.addAttribute("filterMessage",messageFilterDTO);
            model.addAttribute("filterUsers",filterDto);
            model.addAttribute("filterPosts",postFilterDto);
            model.addAttribute("users", users);
            model.addAttribute("messages", messages);
            model.addAttribute("posts", posts);
            model.addAttribute("comments", comments);
            model.addAttribute("tags", tags);
            return "AdminPortal";
        } catch(EntityNotFoundException | AuthorizationException e){
            return "redirect:/auth/accessdenied";
        }
    }

}


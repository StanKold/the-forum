package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.FileUploadUtil;
import com.example.forum.helpers.UserMapper;
import com.example.forum.models.LoginDto;
import com.example.forum.models.RegisterDto;
import com.example.forum.models.User;
import com.example.forum.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationHelper helper;

    private final UserMapper userMapper;

    private final UserService userService;


    @Autowired
    public AuthenticationController(AuthenticationHelper helper, UserMapper userMapper, UserService userService) {
        this.helper = helper;
        this.userMapper = userMapper;
        this.userService = userService;
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



    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }


    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto dto,
                              BindingResult bindingResult,
                              HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "LoginView";
        }
        try {
             User user = helper.verifyAuthentication(dto.getUsername(), dto.getPassword());
             if(user.isBlocked()){
                 return "BlockedView";
             }
             session.setAttribute("loggedUser" ,user.getUsername());
            session.setAttribute("isAdmin", user.isAdmin());
            session.setAttribute("userID",user.getId());
             return"redirect:/auth/login";
        }catch (AuthorizationException e){
            bindingResult.rejectValue("username","auth_error",e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "Register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult,
                                 @RequestParam("file") MultipartFile file) {


        if (bindingResult.hasErrors()) {
            return "Register";
        }

        if (!register.getPassword().equals(register.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
            return "Register";
        }
        try {

            if (!file.isEmpty()) {
                String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                User user = userMapper.createUserFromDto(register);
                user.setAvatar_url("/assets/avatars/" + filename);
                userService.create(user);

                String uploadDir = "src/main/resources/static/assets/avatars/";
                FileUploadUtil.saveFile(uploadDir, filename, file);
            } else {
                User user = userMapper.createUserFromDto(register);
                userService.create(user);
            }
            return "redirect:/auth/login";
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "Register";
        }
    }

    @GetMapping("/logout")
    public String handleLogout() {
        return "Logout";
    }

    @PostMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("loggedUser");
        return "redirect:/";
    }

    @GetMapping("/accessdenied")
    public String handleRejected() {
        return "AccessDeniedView";
    }


}

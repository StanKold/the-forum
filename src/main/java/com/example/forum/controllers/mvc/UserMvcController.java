package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.FileUploadUtil;
import com.example.forum.helpers.UserMapper;
import com.example.forum.models.Phone;
import com.example.forum.models.User;
import com.example.forum.models.UserDto;
import com.example.forum.services.PhoneService;
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
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;

    private final PhoneService phoneService;

    private final UserMapper userMapper;

    private final AuthenticationHelper authenticationHelper;



    @Autowired
    public UserMvcController(UserService userService, PhoneService phoneService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.phoneService = phoneService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
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

    @GetMapping("/{id}/block")
    public String blockUserOrUnblock(@PathVariable int id,  HttpSession session, Model model) {
        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch(EntityNotFoundException e){
            return"redirect:/auth/accessdenied";
        }

        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            userService.blockUser(id, loggedInUser);
            return "redirect:/admin-portal";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }


    @PostMapping("/{id}/phones")
    public String EditPhone(@PathVariable int id,  HttpSession session,
                            @Valid @ModelAttribute("phone") Phone phone,
                            BindingResult bindingResult) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


        if (bindingResult.hasErrors()) {
            return "EditUserView";
        }

        try {
            Phone phoneToUpdate = phoneService.getPhoneByUser(id);
            if (phoneToUpdate!=null) {
                phoneToUpdate.setPhoneNumber(phone.getPhoneNumber());
                phoneService.updatePhone(phoneToUpdate, currentUser);
            }
            else {
                phoneService.addPhone(phone,currentUser);
            }
            return String.format("redirect:/users/%d/update", id);
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("phoneNumber", "duplicate_email", e.getMessage());
            return "EditUserView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/accessdenied";
        }
    }

    @GetMapping("/{id}/phones")
    public String DeletePhone(@PathVariable int id,  HttpSession session,
                            @Valid @ModelAttribute("phone") Phone phone,
                            BindingResult bindingResult) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


        if (bindingResult.hasErrors()) {
            return "EditUserView";
        }

        try {
            Phone phoneToUpdate = phoneService.getPhoneByUser(id);
            phoneService.deletePhone(phoneToUpdate,currentUser);
            return String.format("redirect:/users/%d/update", id);
        } catch (AuthorizationException e) {
            return "redirect:/auth/accessdenied";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        try {
           authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


        try {
            User userToUpdate = userService.getByID(id);
            Phone phone = phoneService.getPhoneByUser(userToUpdate.getId());

            model.addAttribute("phone", Objects.requireNonNullElseGet(phone, Phone::new));

            UserDto userDto = userMapper.toDto(userToUpdate);
            userDto.setPassword("");
            model.addAttribute("userId", id);
            model.addAttribute("userDto", userDto);
            model.addAttribute("updateUrl", String.format("/users/%d/update", id));
            model.addAttribute("isUserAdmin",userToUpdate.isAdmin());
            return "EditUserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id,
                             @Valid @ModelAttribute("userDto") UserDto dto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session, @RequestParam("file") MultipartFile file) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


        if (bindingResult.hasErrors()) {
            return "EditUserView";
        }

        try {

        User userToUpdate = userMapper.fromDto(id,dto);
        authenticationHelper.verifyPassword(userToUpdate.getUsername(),dto.getPassword());
        if (!file.isEmpty()) {
                String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                userToUpdate.setAvatar_url("/assets/avatars/" + filename);
                String uploadDir = "src/main/resources/static/assets/avatars/";
                FileUploadUtil.saveFile(uploadDir, filename, file);
            }

            userService.update(userToUpdate,currentUser);
            return String.format("redirect:/users/%d/update", id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("email", "duplicate_email", e.getMessage());
            return "EditUserView";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("password", "incorrect_password", e.getMessage());
            return "EditUserView";
        }catch (IOException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }


}

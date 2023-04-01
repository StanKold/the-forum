package com.example.forum.controllers.mvc;

import com.example.forum.models.Contact;
import com.example.forum.services.ContactService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/message")
public class ContactMvcController {
 private final ContactService contactService;
@Autowired
    public ContactMvcController(ContactService contactService) {
        this.contactService = contactService;
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
    public String getMessages(Model model) {
        model.addAttribute("contact", new Contact() );
        return "ContactView";
    }

    @PostMapping()
    public String saveMessages(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("contact", contact );
           return "ContactView";
        }
        contactService.saveMessage(contact);
        model.addAttribute("contact", new Contact());
        model.addAttribute("done",true);
        return "ContactView";
    }
    @GetMapping("/{id}/delete")
    public String deleteMessage(@PathVariable int id, HttpSession session){
       if(session.getAttribute("isAdmin")==null || !(boolean)session.getAttribute("isAdmin")){
       return "redirect:auth/accessdenied";
       }
    contactService.deleteMessage(id);
    return "redirect:/admin-portal";

    }

}

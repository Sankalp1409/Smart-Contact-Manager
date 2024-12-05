package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.forms.*;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index() {
        return "redirect:/home";
    }

    // home route
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Sankalp");
        return "home";
    }

    // about
    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("flag", "true");
        return "about";
    }

    // service
    @RequestMapping("/service")
    public String service(Model model) {
        return "service";
    }

    @RequestMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {

        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "signup";
    }

    // Proccessing the form
    @PostMapping("/do-register")
    public String handleForm(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult,
            HttpSession session) {

        System.out.println("Processing Form");
        // fetch the data
        System.out.println(userForm);

        // Validate the Data
        if (bindingResult.hasErrors()) {

            System.out.println("Error Function is invoked!!!");
            return "signup";
        }

        // Save the data
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .phoneNumber(userForm.getPhoneNumber())
        // .about(userForm.getAbout())
        // .profilePic("/images/deafultProfilePicture.png")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setEnabled(false);
        user.setProfilePic("/images/deafultProfilePicture.png");
        userService.saveUser(user);
        System.out.println("User is Saved!!!");

        // Message="Registration SuccessFull"

        Message m = Message.builder().content("Registration Successfull").type(MessageType.green).build();
        session.setAttribute("message", m);
        // Redirect
        return "redirect:/signup";
    }

}
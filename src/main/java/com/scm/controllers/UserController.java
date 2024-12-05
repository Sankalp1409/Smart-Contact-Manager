package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // User DashBoard Page
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String userDashBoard() {
        return "user/dashboard";
    }

    // User Profile Page
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfile(Model model, Authentication authentication) {

        return "user/profile";
    }
    @RequestMapping("/feedback")
    public String feedback() {
        return "user/feedback";
    }
    // User add Contact Page

    // User View Contact Page

    // User Delete Contact Page

    // User Update Contact Page

    // User Search Contact Page

}

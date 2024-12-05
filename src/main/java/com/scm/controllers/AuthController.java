package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/verify-email")
    public String getMethodName(@RequestParam("token") String token, HttpSession session) {
        System.out.print("Verify Email in Controller");

        User user = userRepo.findByEmailToken(token).orElse(null);
        if (user != null) {
            user.setEmailVerified(true);
            user.setEnabled(true);
            userRepo.save(user);
            Message m = Message.builder().content("Accoutn Enabled Successfull").type(MessageType.green).build();
            session.setAttribute("message", m);
        } else {
            Message m = Message.builder().content("Accoutn is disabled, Please verify you account!!")
                    .type(MessageType.red).build();
            session.setAttribute("message", m);
        }
        return "login";
    }

}

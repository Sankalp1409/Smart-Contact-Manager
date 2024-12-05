package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstant;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub
        logger.info("OAuthAuthentication Successfull");

        var oAuth2Token = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistratioId = oAuth2Token.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistratioId);
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        // user.getAttributes().forEach((key, value) -> {
        // logger.info(key + " : " + value);
        // });

        User user1 = new User();
        user1.setUserId(UUID.randomUUID().toString());
        user1.setRoleList(List.of(AppConstant.ROLE_USER));
        user1.setEmailVerified(true);
        user1.setEnabled(true);

        // User Login with Login
        if (authorizedClientRegistratioId.equalsIgnoreCase("google")) {
            String email = user.getAttribute("email").toString();
            String name = user.getAttribute("name").toString();
            String pic = user.getAttribute("picture").toString();

            user1.setEmail(email);
            user1.setName(name);
            user1.setProfilePic(pic);
            user1.setProvider(Providers.GOOGLE);
            user1.setProviderUserId(user1.getName());
            user1.setAbout("This account is created using google authentication");
        } else {
            String email = user.getAttribute("email") != null ? user.getAttribute("email").toString()
                    : user.getAttribute("login").toString() + "@gmail.com";
            String pic = user.getAttribute("avatar_url").toString();
            String name = user.getAttribute("name").toString();
            user1.setEmail(email);
            user1.setName(name);
            user1.setProfilePic(pic);
            user1.setProvider(Providers.GITHUB);
            user1.setProviderUserId(user1.getName());
            user1.setAbout("This account is created using github authentication");
        }

        User user2 = userRepo.findByEmail(user1.getEmail()).orElse(null);
        if (user2 == null) {
            userRepo.save(user1);
            System.out.println("user saved:" + user1.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}

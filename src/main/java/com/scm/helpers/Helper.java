package com.scm.helpers;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        if (authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal) {
            var oAuth2Token = (OAuth2AuthenticationToken) authentication;

            String authorizedClientRegistratioId = oAuth2Token.getAuthorizedClientRegistrationId();
            DefaultOAuth2User oAuthUser = (DefaultOAuth2User) authentication.getPrincipal();
            if (authorizedClientRegistratioId.equalsIgnoreCase("google")) {
                return oAuthUser.getAttribute("email").toString();
            } else if (authorizedClientRegistratioId.equalsIgnoreCase("github")) {
                return oAuthUser.getAttribute("email") != null ? oAuthUser.getAttribute("email").toString()
                        : oAuthUser.getAttribute("login").toString() + "@gmail.com";
            }
            return "";

        } else {
            System.out.println("Getting username from local Database");
            return authentication.getName();
        }
    }

    public static String getEmailVerificationLink(String token) {
        String link = "http://localhost:8081/auth/verify-email?token=" + token;
        return link;
    }
}

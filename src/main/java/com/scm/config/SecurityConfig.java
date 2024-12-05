package com.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm.services.impl.SecurityCustomUserDetailServices;
import com.scm.config.AuthFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
    // @Bean
    // public UserDetailsService userDetailsService() {
    // UserDetails user1 =
    // User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN",
    // "USER")
    // .build();
    // return new InMemoryUserDetailsManager(user1);
    // }

    @Autowired
    private SecurityCustomUserDetailServices userDetailServices;

    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // UserDetail Service k object:
        daoAuthenticationProvider.setUserDetailsService(userDetailServices);

        // Password Encoder k object:
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Restrict the URL'S
        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // Form Default Login
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.failureHandler(authFailureHandler);
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile");
            // formLogin.failureUrl("/login?error=true");

            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

        });

        httpSecurity.csrf(Customizer -> {
            Customizer.disable();
        });

        httpSecurity.logout(formLogout -> {

            formLogout.logoutUrl("/logout");
            formLogout.logoutSuccessUrl("/login?logout=true");
        });

        // OAuth Login Configuration
        httpSecurity.oauth2Login(oauth2Login -> {
            oauth2Login.loginPage("/login");
            oauth2Login.successHandler(oAuthAuthenticationSuccessHandler);
        });
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

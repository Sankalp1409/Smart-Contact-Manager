package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstant;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User u) {

        String userId = UUID.randomUUID().toString();
        u.setUserId(userId);
        u.setPassword(passwordEncoder.encode(u.getPassword()));

        u.setRoleList(List.of(AppConstant.ROLE_USER));

        
        String emailToken = UUID.randomUUID().toString();
        u.setEmailToken(emailToken);
        String link = Helper.getEmailVerificationLink(emailToken);
        User savedUser = userRepo.save(u);
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Email", link);
        
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User u) {
        User user2 = userRepo.findById(u.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user2.setName(u.getName());
        user2.setEmail(u.getEmail());
        user2.setPassword(u.getPassword());
        user2.setAbout(u.getAbout());
        user2.setPhoneNumber(u.getPhoneNumber());
        user2.setProfilePic(u.getProfilePic());
        user2.setEnabled(u.isEnabled());
        user2.setEmailVerified(u.isEmailVerified());
        user2.setPhoneVerified(u.isPhoneVerified());
        user2.setProvider(u.getProvider());
        user2.setProviderUserId(u.getProviderUserId());
        return Optional.ofNullable(userRepo.save(user2));
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        return userRepo.findById(userId).isPresent();
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public List<User> getAllUser() {

        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String e) {
        return userRepo.findByEmail(e).orElse(null);
    }

}

package com.scm.services;

import java.util.*;

import com.scm.entities.User;

public interface UserService {
    User saveUser(User u);

    Optional<User> getUserById(String id);

    Optional<User> updateUser(User u);

    void deleteUser(String id);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    List<User> getAllUser();

    User getUserByEmail(String e);
}

package com.scm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    // Find Contact by user
    List<Contact> findByUser(User user);

    // Find Contact by userId
    @Query("Select c from Contact c where c.user.userId=:userId")
    List<Contact> findByUserId(String userId);

    List<Contact> findByNameContaining(String name);

    List<Contact> findByEmailContaining(String email);

    List<Contact> findByPhoneNumberContaining(String phoneNumber);

}

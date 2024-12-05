package com.scm.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;

@Service
public interface ContactService {

    // save Contact
    Contact saveContact(Contact c);

    // Update Contact
    Contact updateContact(Contact c);

    // Get Contacts
    List<Contact> getContact();

    // getContactById
    Contact getContactById(String id);

    // delete Contact
    void deleteContact(String id);

    // search Contact By Name
    List<Contact> searchContactByName(String name);

    // search Contact By Email
    List<Contact> searchContactByEmail(String email);

    // search Contact By PhoneNumber
    List<Contact> searchContactByphoneNumber(String phoneNumber);

    // get Contact by Userid
    List<Contact> getContactByUserId(String userId);

    // Get Contact by User
    List<Contact> getContactByUser(User u);

}

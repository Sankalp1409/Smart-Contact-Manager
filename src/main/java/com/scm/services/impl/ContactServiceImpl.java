package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepo contactRepo;

    @Override
    public Contact saveContact(Contact c) {
        String id = UUID.randomUUID().toString();
        c.setId(id);
        return contactRepo.save(c);
    }

    @Override
    public Contact updateContact(Contact c) {
        var contact = contactRepo.findById(c.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact Not found!!"));
        contact.setName(c.getName());
        contact.setEmail(c.getEmail());
        contact.setAddress(c.getAddress());
        contact.setDescription(c.getDescription());
        contact.setFavorite(c.isFavorite());
        contact.setPhoneNumber(c.getPhoneNumber());
        contact.setPicture(c.getPicture());
        contact.setCloudinaryImagePublicId(c.getCloudinaryImagePublicId());
        return contactRepo.save(contact);
    }

    @Override
    public List<Contact> getContact() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public void deleteContact(String id) {
        contactRepo.deleteById(id);
    }

    @Override
    public List<Contact> getContactByUserId(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactByUserId'");
    }

    @Override
    public List<Contact> getContactByUser(User u) {
        return contactRepo.findByUser(u);
    }

    @Override
    public List<Contact> searchContactByName(String name) {
        return contactRepo.findByNameContaining(name);
    }

    @Override
    public List<Contact> searchContactByEmail(String email) {
        return contactRepo.findByEmailContaining(email);
    }

    @Override
    public List<Contact> searchContactByphoneNumber(String phoneNumber) {
        return contactRepo.findByPhoneNumberContaining(phoneNumber);
    }

}

package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/add")
    public String addContactView(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "user/addContact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
            Authentication authentication, HttpSession session) {
        System.out.println(contactForm);

        if (bindingResult.hasErrors()) {
            session.setAttribute("message",
                    Message.builder().content("Please fill the required detials.").type(MessageType.red).build());
            return "/user/addContact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User u = userService.getUserByEmail(username);
        String filename = UUID.randomUUID().toString();
        String imageUrl = imageService.uploadImage(contactForm.getPicture(), filename);
        logger.info("file info:" + contactForm.getPicture().getOriginalFilename());

        // Save Contact Details
        Contact c = new Contact();
        c.setName(contactForm.getName());
        c.setEmail(contactForm.getEmail());
        c.setPhoneNumber(contactForm.getPhoneNumber());
        c.setAddress(contactForm.getAddress());
        c.setDescription(contactForm.getDescription());
        c.setFavorite(contactForm.isFavorite());
        c.setPicture(imageUrl);
        c.setUser(u);
        c.setCloudinaryImagePublicId(filename);
        contactService.saveContact(c);
        session.setAttribute("message",
                Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());
        return "redirect:/user/contact/add";
    }

    @RequestMapping
    public String viewContact(Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        List<Contact> contactList = contactService.getContactByUser(user);
        model.addAttribute("contact", contactList);
        model.addAttribute("ContactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm, Model model,
            Authentication authentication) {

        List<Contact> contacts;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            System.out.println("In a field");
            contacts = contactService.searchContactByName(contactSearchForm.getKeyword());
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            contacts = contactService.searchContactByEmail(contactSearchForm.getKeyword());
        } else if (contactSearchForm.getField().equalsIgnoreCase("phoneNumber")) {
            contacts = contactService.searchContactByphoneNumber(contactSearchForm.getKeyword());
        } else {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            User user = userService.getUserByEmail(username);
            contacts = contactService.getContactByUser(user);
        }
        model.addAttribute("contact", contacts);
        model.addAttribute("ContactSearchForm", contactSearchForm);
        logger.info("Contact List {}", contacts);

        return "user/search";
    }

    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return "redirect:/user/contact";
    }

    @RequestMapping("/view/{id}")
    public String viewContact(@PathVariable String id, Model model) {

        var contact = contactService.getContactById(id);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setPicUrl(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("id", id);
        return "/user/updateView";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String updateContact(@Valid @PathVariable String id, @ModelAttribute ContactForm contactForm, Model model,
            BindingResult bindingResult) {
        System.out.println("Binding" + bindingResult);
        if (bindingResult.hasErrors())
            return "user/updateView";
        var contact = contactService.getContactById(id);
        contact.setId(id);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        // process image
        if (contactForm.getPicture() != null && !contactForm.getPicture().isEmpty()) {
            String fileName = UUID.randomUUID().toString();
            String imgUrl = imageService.uploadImage(contactForm.getPicture(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setPicture(imgUrl);
            contactForm.setPicUrl(imgUrl);
        }

        var updateContact = contactService.updateContact(contact);
        // model.addAttribute("contactForm", contactForm);
        return "redirect:/user/contact";
    }

}
